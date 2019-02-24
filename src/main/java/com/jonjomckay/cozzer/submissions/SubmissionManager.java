package com.jonjomckay.cozzer.submissions;

import com.jonjomckay.cozzer.submissions.tests.TestReportParserFactory;
import com.jonjomckay.cozzer.submissions.tests.TestReportType;
import org.jdbi.v3.core.Handle;
import org.jooby.Upload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SubmissionManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(SubmissionManager.class);

    private final Handle handle;
    private final TestReportParserFactory testReportParserFactory;

    @Inject
    public SubmissionManager(Handle handle, TestReportParserFactory testReportParserFactory) {
        this.handle = handle;
        this.testReportParserFactory = testReportParserFactory;
    }

    void assertSubmissionExists(UUID id) {
        var exists = handle.createQuery("SELECT EXISTS (SELECT 1 FROM submissions WHERE id = :id)")
                .bind("id", id)
                .mapTo(boolean.class)
                .findOnly();

        if (exists == false) {
            throw Problem.valueOf(Status.NOT_FOUND, "No submission could be found with that ID");
        }
    }

    // TODO: Extract this into some interface or something, so we can ensure we're ending up with a consistent format
    public void submitSurefireResults(UUID submission, List<Upload> results) {
        assertSubmissionExists(submission);

        var reportParser = testReportParserFactory.create(TestReportType.Surefire);

        var suites = results.stream()
                .flatMap(upload -> {
                    try {
                        return reportParser.parse(new FileInputStream(upload.file()));
                    } catch (IOException e) {
                        LOGGER.error("Unable to parse the test results", e);

                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        for (var suite : suites) {
            var id = handle.createQuery("INSERT INTO results_test_suites (id, submission_id, name, duration) VALUES (:id, :submission, :name, :duration) ON CONFLICT (submission_id, name) DO UPDATE SET duration = :duration RETURNING id")
                    .bind("id", UUID.randomUUID())
                    .bind("submission", submission)
                    .bind("name", suite.getName())
                    .bind("duration", suite.getDuration())
                    .mapTo(UUID.class)
                    .findOnly();

            var caseBatch = handle.prepareBatch("INSERT INTO results_test_cases (id, suite_id, duration, is_errored, is_failed, is_skipped, is_successful, failure_message, name) VALUES (:id, :suite, :duration, :isErrored, :isFailed, :isSkipped, :isSuccessful, :failureMessage, :name) ON CONFLICT (suite_id, name) DO UPDATE SET duration = :duration, is_errored = :isErrored, is_failed = :isFailed, is_skipped = :isSkipped, is_successful = :isSuccessful, failure_message = :failureMessage");

            for (var testCase : suite.getTestCases()) {
                // Insert any given test cases, updating ones that already exist (e.g. if the same build was re-run)
                caseBatch
                        .bind("id", UUID.randomUUID())
                        .bind("suite", id)
                        .bind("duration", testCase.getDuration())
                        .bind("isErrored", testCase.isErrored())
                        .bind("isFailed", testCase.isFailed())
                        .bind("isSkipped", testCase.isSkipped())
                        .bind("isSuccessful", testCase.isSuccessful())
                        .bind("failureMessage", testCase.getFailureMessage())
                        .bind("name", testCase.getName())
                        .add();
            }

            caseBatch.execute();
        }

        for (var upload : results) {
            try {
                upload.close();
            } catch (IOException e) {
                LOGGER.error("Unable to close the upload {}", upload.name(), e);
            }
        }
    }
}

package com.jonjomckay.testmate.submissions;

import com.jonjomckay.testmate.submissions.tests.TestCase;
import com.jonjomckay.testmate.submissions.tests.TestSuite;
import org.apache.maven.plugin.surefire.log.api.ConsoleLoggerDecorator;
import org.apache.maven.plugins.surefire.report.ReportTestCase;
import org.apache.maven.plugins.surefire.report.ReportTestSuite;
import org.apache.maven.plugins.surefire.report.TestSuiteXmlParser;
import org.jdbi.v3.core.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.inject.Inject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SubmissionManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(SubmissionManager.class);

    private final Handle handle;

    @Inject
    public SubmissionManager(Handle handle) {
        this.handle = handle;
    }

    UUID findSubmissionProject(UUID id) {
        var project = handle.createQuery("SELECT project_id FROM submissions WHERE id = :id")
                .bind("id", id)
                .mapTo(UUID.class)
                .findFirst();

        return project
                .orElseThrow(() -> Problem.valueOf(Status.NOT_FOUND, "No submission could be found with that ID"));
    }

    // TODO: Extract this into some interface or something, so we can ensure we're ending up with a consistent format
    public void submitJunitResults(UUID submission, InputStream results) {
        var project = findSubmissionProject(submission);

        var parser = new TestSuiteXmlParser(new ConsoleLoggerDecorator(LOGGER));

        List<ReportTestSuite> testSuites;
        try {
            testSuites = parser.parse(new InputStreamReader(results));
        } catch (Exception e) {
            LOGGER.error("Unable to parse the incoming JUnit report", e);

            throw new RuntimeException("Unable to parse the incoming JUnit report", e);
        }

        if (testSuites.isEmpty()) {
            return;
        }

        var suites = testSuites.stream()
                .map(SubmissionManager::createTestSuite)
                .collect(Collectors.toList());

        for (var suite : suites) {
            var id = handle.createQuery("INSERT INTO results_test_suites (id, project_id, submission_id, name, duration) VALUES (:id, :project, :submission, :name, :duration) ON CONFLICT (submission_id, name) DO UPDATE SET duration = :duration RETURNING id")
                    .bind("id", UUID.randomUUID())
                    .bind("project", project)
                    .bind("submission", submission)
                    .bind("name", suite.getName())
                    .bind("duration", suite.getDuration())
                    .mapTo(UUID.class)
                    .findOnly();

            for (var testCase : suite.getTestCases()) {
                // Insert any given test cases, updating ones that already exist (e.g. if the same build was re-run)
                handle.createUpdate("INSERT INTO results_test_cases (id, suite_id, duration, is_errored, is_failed, is_skipped, is_successful, failure_message, name) VALUES (:id, :suite, :duration, :isErrored, :isFailed, :isSkipped, :isSuccessful, :failureMessage, :name) ON CONFLICT (suite_id, name) DO UPDATE SET duration = :duration, is_errored = :isErrored, is_failed = :isFailed, is_skipped = :isSkipped, is_successful = :isSuccessful, failure_message = :failureMessage")
                        .bind("id", UUID.randomUUID())
                        .bind("suite", id)
                        .bind("duration", testCase.getDuration())
                        .bind("isErrored", testCase.isErrored())
                        .bind("isFailed", testCase.isFailed())
                        .bind("isSkipped", testCase.isSkipped())
                        .bind("isSuccessful", testCase.isSuccessful())
                        .bind("failureMessage", testCase.getFailureMessage())
                        .bind("name", testCase.getName())
                        .execute();
            }
        }
    }

    private static TestSuite createTestSuite(ReportTestSuite suite) {
        var testCases = suite.getTestCases().stream()
                .map(SubmissionManager::createTestCase)
                .collect(Collectors.toList());

        return new TestSuite(suite.getFullClassName(), suite.getTimeElapsed(), testCases);
    }

    private static TestCase createTestCase(ReportTestCase reportTestCase) {
        TestCase testCase = new TestCase();
        testCase.setDuration(reportTestCase.getTime());
        testCase.setErrored(reportTestCase.hasError());
        testCase.setFailed(reportTestCase.hasFailure());
        testCase.setFailureMessage(reportTestCase.getFailureMessage());
        testCase.setName(reportTestCase.getName());
        testCase.setSkipped(reportTestCase.hasSkipped());
        testCase.setSuccessful(reportTestCase.isSuccessful());

        return testCase;
    }
}

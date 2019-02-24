package com.jonjomckay.cozzer.projects.submissions.tests;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import org.jdbi.v3.core.result.RowView;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SubmissionTestManager {
    private final Handle handle;

    @Inject
    public SubmissionTestManager(Handle handle) {
        this.handle = handle;
    }

    public List<SubmissionTestSuiteListResponse> listSubmissionTestSuites(UUID submission) {
        return handle.createQuery("SELECT s.id, s.duration, s.name, COUNT(c.id) number_of_test_cases FROM results_test_suites s LEFT JOIN results_test_cases c ON c.suite_id = s.id WHERE s.submission_id = :id GROUP BY s.id ORDER BY s.name")
                .bind("id", submission)
                .mapToBean(SubmissionTestSuiteListResponse.class)
                .list();
    }

    public Optional<SubmissionTestSuiteResponse> loadSubmissionTestSuite(UUID submission, UUID id) {
        return handle.createQuery("SELECT s.id s_id, s.duration s_duration, s.name s_name, c.id c_id, c.duration c_duration, c.is_errored c_errored, c.is_failed c_failed, c.failure_message c_failure_message, c.name c_name, c.is_skipped c_skipped, c.is_successful c_successful FROM results_test_suites s LEFT JOIN results_test_cases c ON c.suite_id = s.id WHERE s.id = :id AND s.submission_id = :submission ORDER BY c.name")
                .bind("id", id)
                .bind("submission", submission)
                .registerRowMapper(BeanMapper.factory(SubmissionTestSuiteResponse.class, "s"))
                .registerRowMapper(BeanMapper.factory(SubmissionTestSuiteResponse.TestCase.class, "c"))
                .reduceRows((Map<UUID, SubmissionTestSuiteResponse> map, RowView rowView) -> {
                    SubmissionTestSuiteResponse suite = map.computeIfAbsent(
                            rowView.getColumn("s_id", UUID.class),
                            suiteId -> rowView.getRow(SubmissionTestSuiteResponse.class)
                    );

                    if (rowView.getColumn("c_id", UUID.class) != null) {
                        suite.getTestCases().add(rowView.getRow(SubmissionTestSuiteResponse.TestCase.class));
                    }
                })
                .findFirst();
    }
}

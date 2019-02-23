package com.jonjomckay.testmate.projects.tests;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import org.jdbi.v3.core.result.RowView;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ProjectTestManager {
    private final Handle handle;

    @Inject
    public ProjectTestManager(Handle handle) {
        this.handle = handle;
    }

    public List<ProjectTestSuiteListResponse> listProjectTestSuites(UUID project) {
        return handle.createQuery("SELECT id, duration, name FROM results_test_suites WHERE project_id = :id ORDER BY name")
                .bind("id", project)
                .mapToBean(ProjectTestSuiteListResponse.class)
                .list();
    }

    public Optional<ProjectTestSuiteResponse> loadProjectTestSuite(UUID project, UUID id) {
        return handle.createQuery("SELECT s.id s_id, s.duration s_duration, s.name s_name, c.id c_id, c.duration c_duration, c.is_errored c_errored, c.is_failed c_failed, c.failure_message c_failure_message, c.name c_name, c.is_skipped c_skipped, c.is_successful c_successful FROM results_test_suites s LEFT JOIN results_test_cases c ON c.suite_id = s.id WHERE s.project_id = :project AND s.id = :id ORDER BY c.name")
                .bind("project", project)
                .bind("id", id)
                .registerRowMapper(BeanMapper.factory(ProjectTestSuiteResponse.class, "s"))
                .registerRowMapper(BeanMapper.factory(ProjectTestSuiteResponse.TestCase.class, "c"))
                .reduceRows((Map<UUID, ProjectTestSuiteResponse> map, RowView rowView) -> {
                    ProjectTestSuiteResponse suite = map.computeIfAbsent(
                            rowView.getColumn("s_id", UUID.class),
                            suiteId -> rowView.getRow(ProjectTestSuiteResponse.class)
                    );

                    if (rowView.getColumn("c_id", UUID.class) != null) {
                        suite.getTestCases().add(rowView.getRow(ProjectTestSuiteResponse.TestCase.class));
                    }
                })
                .findFirst();
    }
}

package com.jonjomckay.testmate.projects.submissions;

import org.jdbi.v3.core.Handle;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProjectSubmissionManager {
    private final Handle handle;

    @Inject
    public ProjectSubmissionManager(Handle handle) {
        this.handle = handle;
    }

    public ProjectSubmissionCreateResponse createSubmission(UUID project, ProjectSubmissionCreateRequest request) {
        // Generate a submission ID
        var id = UUID.randomUUID();

        // Create an empty record in the database
        handle.createUpdate("INSERT INTO submissions (id, project_id, external_key) VALUES (:id, :project, :externalKey)")
                .bind("id", id)
                .bind("project", project)
                .bind("externalKey", request.getExternalKey())
                .execute();

        // Require a submission ID on each type of submission upload (JUnit reports, coverage, metrics, etc)

        return new ProjectSubmissionCreateResponse()
                .setId(id);
    }

    public Optional<ProjectSubmissionResponse> loadSubmission(UUID project, UUID id) {
        return handle.createQuery("SELECT id, external_key, created_at FROM submissions WHERE project_id = :project AND id = :id")
                .bind("project", project)
                .bind("id", id)
                .mapToBean(ProjectSubmissionResponse.class)
                .findFirst();
    }

    public List<ProjectSubmissionResponse> listSubmissions(UUID project) {
        return handle.createQuery("SELECT id, external_key, created_at FROM submissions WHERE project_id = :project ORDER BY created_at DESC")
                .bind("project", project)
                .mapToBean(ProjectSubmissionResponse.class)
                .list();
    }
}

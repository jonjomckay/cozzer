package com.jonjomckay.cozzer.projects.submissions;

import com.google.common.base.Strings;
import org.jdbi.v3.core.Handle;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

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
        if (Strings.isNullOrEmpty(request.getExternalKey())) {
            throw Problem.valueOf(Status.BAD_REQUEST, "An external key is required to create a submission");
        }

        var exists = handle.createQuery("SELECT EXISTS (SELECT 1 FROM submissions WHERE project_id = :project AND external_key = :externalKey)")
                .bind("project", project)
                .bind("externalKey", request.getExternalKey())
                .mapTo(boolean.class)
                .findOnly();

        if (exists) {
            throw Problem.valueOf(Status.CONFLICT, "A submission already exists with that external key");
        }

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
                .setId(id)
                .setExternalKey(request.getExternalKey());
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

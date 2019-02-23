package com.jonjomckay.testmate.projects.submissions;

import org.jdbi.v3.core.Handle;

import javax.inject.Inject;
import java.util.UUID;

public class ProjectSubmissionManager {
    private final Handle handle;

    @Inject
    public ProjectSubmissionManager(Handle handle) {
        this.handle = handle;
    }

    public ProjectSubmissionCreateResponse createSubmission(UUID project) {
        // Generate a submission ID
        var id = UUID.randomUUID();

        // Create an empty record in the database
        handle.createUpdate("INSERT INTO submissions (id, project_id) VALUES (:id, :project)")
                .bind("id", id)
                .bind("project", project)
                .execute();

        // Require a submission ID on each type of submission upload (JUnit reports, coverage, metrics, etc)

        return new ProjectSubmissionCreateResponse()
                .setId(id);
    }
}

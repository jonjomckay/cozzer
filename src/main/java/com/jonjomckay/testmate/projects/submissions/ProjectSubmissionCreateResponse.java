package com.jonjomckay.testmate.projects.submissions;

import java.util.UUID;

public class ProjectSubmissionCreateResponse {
    private UUID id;

    public UUID getId() {
        return id;
    }

    public ProjectSubmissionCreateResponse setId(UUID id) {
        this.id = id;
        return this;
    }
}

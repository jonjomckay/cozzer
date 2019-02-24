package com.jonjomckay.testmate.projects.submissions;

import java.util.UUID;

public class ProjectSubmissionCreateResponse {
    private UUID id;
    private String externalKey;

    public UUID getId() {
        return id;
    }

    public ProjectSubmissionCreateResponse setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getExternalKey() {
        return externalKey;
    }

    public ProjectSubmissionCreateResponse setExternalKey(String externalKey) {
        this.externalKey = externalKey;
        return this;
    }
}

package com.jonjomckay.cozzer.projects;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Project {
    private UUID id;
    private String slug;
    private String name;
    private OffsetDateTime lastSubmissionAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OffsetDateTime getLastSubmissionAt() {
        return lastSubmissionAt;
    }

    public void setLastSubmissionAt(OffsetDateTime lastSubmissionAt) {
        this.lastSubmissionAt = lastSubmissionAt;
    }
}

package com.jonjomckay.testmate.projects.tests;

import java.util.UUID;

public class ProjectTestSuiteListResponse {
    private UUID id;
    private float duration;
    private String name;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

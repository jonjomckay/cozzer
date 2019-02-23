package com.jonjomckay.testmate.projects.tests;

import java.util.UUID;

public class ProjectTestSuiteListResponse {
    private UUID id;
    private float duration;
    private String name;
    private long numberOfTestCases;

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

    public long getNumberOfTestCases() {
        return numberOfTestCases;
    }

    public void setNumberOfTestCases(long numberOfTestCases) {
        this.numberOfTestCases = numberOfTestCases;
    }
}

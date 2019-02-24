package com.jonjomckay.testmate.projects.submissions.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SubmissionTestSuiteResponse {
    private UUID id;
    private float duration;
    private String name;
    private List<TestCase> testCases = new ArrayList<>();

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

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public static class TestCase {
        private float duration;
        private boolean errored;
        private boolean failed;
        private String failureMessage;
        private UUID id;
        private String name;
        private boolean skipped;
        private boolean successful;

        public float getDuration() {
            return duration;
        }

        public void setDuration(float duration) {
            this.duration = duration;
        }

        public boolean isErrored() {
            return errored;
        }

        public void setErrored(boolean errored) {
            this.errored = errored;
        }

        public boolean isFailed() {
            return failed;
        }

        public void setFailed(boolean failed) {
            this.failed = failed;
        }

        public String getFailureMessage() {
            return failureMessage;
        }

        public void setFailureMessage(String failureMessage) {
            this.failureMessage = failureMessage;
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSkipped() {
            return skipped;
        }

        public void setSkipped(boolean skipped) {
            this.skipped = skipped;
        }

        public boolean isSuccessful() {
            return successful;
        }

        public void setSuccessful(boolean successful) {
            this.successful = successful;
        }
    }
}

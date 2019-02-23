package com.jonjomckay.testmate.submissions.tests;

public class TestCase {
    private float duration;
    private boolean errored;
    private boolean failed;
    private String failureMessage;
    private String name;
    private boolean skipped;
    private boolean successful;

    public TestCase() {
    }

    public TestCase(float duration, boolean errored, boolean failed, String failureMessage, String name, boolean skipped, boolean successful) {
        this.duration = duration;
        this.errored = errored;
        this.failed = failed;
        this.failureMessage = failureMessage;
        this.name = name;
        this.skipped = skipped;
        this.successful = successful;
    }

    public float getDuration() {
        return duration;
    }

    public TestCase setDuration(float duration) {
        this.duration = duration;
        return this;
    }

    public boolean isErrored() {
        return errored;
    }

    public TestCase setErrored(boolean errored) {
        this.errored = errored;
        return this;
    }

    public boolean isFailed() {
        return failed;
    }

    public TestCase setFailed(boolean failed) {
        this.failed = failed;
        return this;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public TestCase setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
        return this;
    }

    public String getName() {
        return name;
    }

    public TestCase setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isSkipped() {
        return skipped;
    }

    public TestCase setSkipped(boolean skipped) {
        this.skipped = skipped;
        return this;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public TestCase setSuccessful(boolean successful) {
        this.successful = successful;
        return this;
    }
}

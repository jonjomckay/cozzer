package com.jonjomckay.cozzer.submissions.tests;

import java.util.List;

public class TestSuite {
    private String name;
    private float duration;
    private List<TestCase> testCases;

    public TestSuite(String name, float duration, List<TestCase> testCases) {
        this.name = name;
        this.duration = duration;
        this.testCases = testCases;
    }

    public String getName() {
        return name;
    }

    public float getDuration() {
        return duration;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }
}

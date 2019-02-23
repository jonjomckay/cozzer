package com.jonjomckay.testmate.submissions;

import org.junit.Test;

import java.util.UUID;

public class SubmissionManagerTests {
    @Test
    public void testSubmitJunitResultsWithFailure() {
        var manager = new SubmissionManager(null);

        manager.submitJunitResults(UUID.randomUUID(), getClass().getClassLoader().getResourceAsStream("junit-failure.xml"));
    }
}

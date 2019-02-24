package com.jonjomckay.cozzer.submissions.tests;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertThat;

public class TestReportParserFactoryTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private final TestReportParserFactory factory = new TestReportParserFactory();

    @Test
    public void testFactoryCreatesCorrectImplementations() {
        assertThat(factory.create(TestReportType.Surefire), Matchers.instanceOf(SurefireReportParser.class));
    }

    @Test
    public void testFactoryWithNoType() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("No test report type was given");
        factory.create(null);
    }

    @Test
    public void testFactoryWithUnsupportedType() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("not a supported test report type");
        factory.create(TestReportType.Unknown);
    }
}

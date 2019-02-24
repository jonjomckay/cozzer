package com.jonjomckay.cozzer.submissions.tests;

import org.apache.maven.plugins.surefire.report.ReportTestCase;
import org.apache.maven.plugins.surefire.report.ReportTestSuite;
import org.apache.maven.plugins.surefire.report.TestSuiteXmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SurefireReportParser implements TestReportParser {
    private final static Logger LOGGER = LoggerFactory.getLogger(SurefireReportParser.class);

    private final TestSuiteXmlParser parser;

    public SurefireReportParser(TestSuiteXmlParser parser) {
        this.parser = parser;
    }

    @Override
    public Stream<TestSuite> parse(InputStream report) {
        List<ReportTestSuite> testSuites;
        try {
            testSuites = parser.parse(new InputStreamReader(report));
        } catch (Exception e) {
            LOGGER.error("Unable to parse the incoming Surefire report", e);

            throw Problem.valueOf(Status.BAD_REQUEST, "Unable to parse the incoming Surefire report");
        }

        return testSuites.stream().map(SurefireReportParser::createTestSuite);
    }

    private static TestSuite createTestSuite(ReportTestSuite suite) {
        var testCases = suite.getTestCases().stream()
                .map(SurefireReportParser::createTestCase)
                .collect(Collectors.toList());

        return new TestSuite(suite.getFullClassName(), suite.getTimeElapsed(), testCases);
    }

    private static TestCase createTestCase(ReportTestCase reportTestCase) {
        TestCase testCase = new TestCase();
        testCase.setDuration(reportTestCase.getTime());
        testCase.setErrored(reportTestCase.hasError());
        testCase.setFailed(reportTestCase.hasFailure());
        testCase.setFailureMessage(reportTestCase.getFailureMessage());
        testCase.setName(reportTestCase.getName());
        testCase.setSkipped(reportTestCase.hasSkipped());
        testCase.setSuccessful(reportTestCase.isSuccessful());

        return testCase;
    }
}

package com.jonjomckay.testmate.submissions.tests;

import org.apache.maven.plugin.surefire.log.api.ConsoleLoggerDecorator;
import org.apache.maven.plugins.surefire.report.TestSuiteXmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestReportParserFactory {
    private final static Logger LOGGER = LoggerFactory.getLogger(TestReportParserFactory.class);

    public TestReportParser create(TestReportType type) {
        switch (type) {
            case Surefire:
                return new SurefireReportParser(new TestSuiteXmlParser(new ConsoleLoggerDecorator(LOGGER)));
            default:
                throw new RuntimeException(type + " is not a supported test report type");
        }
    }
}

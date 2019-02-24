package com.jonjomckay.cozzer.submissions.tests;

import org.apache.maven.plugin.surefire.log.api.ConsoleLoggerDecorator;
import org.apache.maven.plugins.surefire.report.TestSuiteXmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestReportParserFactory {
    private final static Logger LOGGER = LoggerFactory.getLogger(TestReportParserFactory.class);

    public TestReportParser create(TestReportType type) {
        if (type == null) {
            throw new RuntimeException("No test report type was given");
        }

        switch (type) {
            case Surefire:
                return new SurefireReportParser(new TestSuiteXmlParser(new ConsoleLoggerDecorator(LOGGER)));
            default:
                throw new RuntimeException(type + " is not a supported test report type");
        }
    }
}

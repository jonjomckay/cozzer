package com.jonjomckay.cozzer.submissions.tests;

import java.io.InputStream;
import java.util.stream.Stream;

public interface TestReportParser {
    Stream<TestSuite> parse(InputStream report);
}

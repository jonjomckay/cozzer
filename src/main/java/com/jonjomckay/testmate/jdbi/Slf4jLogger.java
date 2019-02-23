package com.jonjomckay.testmate.jdbi;

import org.jdbi.v3.core.statement.SqlLogger;
import org.jdbi.v3.core.statement.StatementContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jLogger implements SqlLogger {
    private final static Logger LOGGER = LoggerFactory.getLogger(Slf4jLogger.class);

    @Override
    public void logBeforeExecution(StatementContext context) {
        LOGGER.debug("Executing SQL query {}", context.getRenderedSql());
    }
}

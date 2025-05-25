package org.gardar.analyzer.parser;

import org.gardar.analyzer.domain.LogEntry;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DefaultLogParserTest {

    private final LogParser parser = new DefaultLogParser();

    private static final String SAMPLE =
            "1.1.1.1 - - [14/06/2017:16:47:02 +1000] " +
                    "\"PUT /rest/v1.4/ HTTP/1.1\" 200 2 44.510983 " +
                    "\"-\" \"@list-item-updater\" prio:0";

    @Test
    void parsesValidLine() {
        LogEntry e = parser.parse(SAMPLE).orElseThrow();
        LocalDateTime ts = LocalDateTime.of(2017, 6, 14, 16, 47, 2);

        assertEquals(ts, e.timestamp());
        assertEquals(200, e.httpStatusCode());
        assertEquals(44.510983, e.durationMs(), 1e-6);
    }

    @Test
    void returnsEmptyLine() {
        assertTrue(parser.parse("some line").isEmpty());
    }
}
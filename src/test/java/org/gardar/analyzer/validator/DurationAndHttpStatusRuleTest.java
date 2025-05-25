package org.gardar.analyzer.validator;

import org.gardar.analyzer.domain.LogEntry;
import org.gardar.analyzer.validator.rule.DurationAndHttpStatusRule;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DurationAndHttpStatusRuleTest {

    private final DurationAndHttpStatusRule rule = new DurationAndHttpStatusRule(45.0);

    @Test
    void successWhenOkAndFast() {
        LogEntry e = new LogEntry(LocalDateTime.now(), 200, 12.3);
        assertTrue(rule.isSuccess(e));
    }

    @Test
    void failWhen5xx() {
        LogEntry e = new LogEntry(LocalDateTime.now(), 503, 10.0);
        assertFalse(rule.isSuccess(e));
    }

    @Test
    void failWhenSlow() {
        LogEntry e = new LogEntry(LocalDateTime.now(), 200, 120.0);
        assertFalse(rule.isSuccess(e));
    }
}
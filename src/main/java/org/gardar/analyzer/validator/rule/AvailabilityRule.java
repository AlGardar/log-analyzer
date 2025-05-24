package org.gardar.analyzer.validator.rule;

import org.gardar.analyzer.domain.LogEntry;

public interface AvailabilityRule {
    boolean isSuccess(LogEntry entry);
}
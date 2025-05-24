package org.gardar.analyzer.validator.rule;

import lombok.RequiredArgsConstructor;
import org.gardar.analyzer.domain.LogEntry;

@RequiredArgsConstructor
public class DurationAndStatusRule implements AvailabilityRule {

    private final double timeThresholdMs;

    @Override
    public boolean isSuccess(LogEntry entry) {
        int code = entry.httpStatusCode();
        return (code < 500 || code >= 600) && entry.durationMs() <= timeThresholdMs;
    }
}

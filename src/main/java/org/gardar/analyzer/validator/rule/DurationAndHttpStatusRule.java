package org.gardar.analyzer.validator.rule;

import lombok.RequiredArgsConstructor;
import org.gardar.analyzer.domain.LogEntry;

@RequiredArgsConstructor
public class DurationAndHttpStatusRule implements AvailabilityRule {

    private final double durationThresholdMs;

    @Override
    public boolean isSuccess(LogEntry entry) {
        int httpCode = entry.httpStatusCode();
        return (httpCode < 500 || httpCode >= 600) && entry.durationMs() <= durationThresholdMs;
    }
}

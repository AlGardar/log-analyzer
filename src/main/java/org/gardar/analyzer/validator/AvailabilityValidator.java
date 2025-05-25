package org.gardar.analyzer.validator;

import lombok.RequiredArgsConstructor;
import org.gardar.analyzer.domain.OneSecondStats;
import org.gardar.analyzer.domain.LogEntry;
import org.gardar.analyzer.validator.rule.AvailabilityRule;

@RequiredArgsConstructor
public class AvailabilityValidator {

    private final AvailabilityRule rule;

    public boolean isSuccess(LogEntry entry) {
        return rule.isSuccess(entry);
    }

    public double availabilityPercent(OneSecondStats s) {
        return s.getOneSecondTotalRequests() == 0 ? 100.0 : 100.0 * s.getOneSecondSuccessRequests() / s.getOneSecondTotalRequests();
    }
}
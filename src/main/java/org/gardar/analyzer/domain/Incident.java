package org.gardar.analyzer.domain;

import java.time.LocalDateTime;

public record Incident(LocalDateTime start,
                       LocalDateTime end,
                       double availabilityPercent) {
    @Override
    public String toString() {
        return "%s %s %.1f".formatted(start.toLocalTime(),
                end.toLocalTime(),
                availabilityPercent);
    }
}
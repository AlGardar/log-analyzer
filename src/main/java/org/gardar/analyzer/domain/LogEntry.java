package org.gardar.analyzer.domain;

import java.time.LocalDateTime;

public record LogEntry(LocalDateTime timestamp,
                       int httpStatusCode,
                       double durationMs) {
}

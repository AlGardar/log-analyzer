package org.gardar.analyzer.parser;

import org.gardar.analyzer.domain.LogEntry;

import java.util.Optional;

public interface LogParser {
    Optional<LogEntry> parse(String rawLine);
}
package org.gardar.analyzer.parser;

import lombok.extern.slf4j.Slf4j;
import org.gardar.analyzer.domain.LogEntry;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultLogParser implements LogParser {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy:HH:mm:ss Z");

    private static final Pattern LOG_PATTERN = Pattern.compile(
            "^\\S+ \\S+ \\S+ \\[([^]]+)] \"[A-Z]+ [^\"]+\" (\\d{3}) \\d+ ([\\d.]+)"
    );

    @Override
    public Optional<LogEntry> parse(String rowLine) {
        Matcher m = LOG_PATTERN.matcher(rowLine);
        if (!m.find()) {
            return Optional.empty();
        }

        try {
            LocalDateTime ts = ZonedDateTime.parse(m.group(1), FORMATTER)
                    .toLocalDateTime()
                    .withNano(0);
            int status = Integer.parseInt(m.group(2));
            double duration = Double.parseDouble(m.group(3));

            return Optional.of(new LogEntry(ts, status, duration));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
package org.gardar.analyzer.processor;

import lombok.RequiredArgsConstructor;
import org.gardar.analyzer.detector.IncidentDetector;
import org.gardar.analyzer.domain.OneSecondStats;
import org.gardar.analyzer.domain.LogEntry;
import org.gardar.analyzer.parser.DefaultLogParser;
import org.gardar.analyzer.validator.AvailabilityValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
public class StreamingLogProcessor {

    private final DefaultLogParser parser;
    private final IncidentDetector detector;
    private final AvailabilityValidator validator;


    public void process(InputStream in) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            LocalDateTime currentSecond = null;
            OneSecondStats stats = new OneSecondStats();

            while ((line = br.readLine()) != null) {
                Optional<LogEntry> maybe = parser.parse(line);
                if (maybe.isEmpty()) {
                    continue;
                }
                LogEntry entry = maybe.get();

                boolean isSuccessful = validator.isSuccess(entry);

                if (currentSecond == null) {
                    currentSecond = entry.timestamp();
                }

                if (entry.timestamp().equals(currentSecond)) {
                    stats.record(isSuccessful);
                } else {
                    detector.detectIncident(currentSecond, stats);

                    currentSecond = entry.timestamp();
                    stats.reset();
                    stats.record(isSuccessful);
                }
            }

            if (currentSecond != null) {
                detector.detectIncident(currentSecond, stats);
            }
            detector.finish();
        }
    }
}
package org.gardar.analyzer.processor;

import org.gardar.analyzer.detector.IncidentDetector;
import org.gardar.analyzer.parser.DefaultLogParser;
import org.gardar.analyzer.report.IncidentReporter;
import org.gardar.analyzer.validator.AvailabilityValidator;
import org.gardar.analyzer.validator.rule.DurationAndHttpStatusRule;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class StreamingLogProcessorTest {

    private static final String LINES = String.join("\n",
            // ok
            "1.1.1.1 - - [01/01/2025:00:00:00 +1000] \"GET / HTTP/1.1\" 200 0 10 \"-\" \"agent\" prio:0",
            // 5xx – должно запустить инцидент
            "1.1.1.1 - - [01/01/2025:00:00:01 +1000] \"GET / HTTP/1.1\" 503 0 10 \"-\" \"agent\" prio:0",
            // восстановление
            "1.1.1.1 - - [01/01/2025:00:00:02 +1000] \"GET / HTTP/1.1\" 200 0 10 \"-\" \"agent\" prio:0"
    ) + "\n";

    @Test
    void smokeEndToEnd() throws Exception {
        AtomicInteger incidents = new AtomicInteger();
        IncidentReporter capture = i -> incidents.incrementAndGet();

        StreamingLogProcessor p = new StreamingLogProcessor(
                new DefaultLogParser(),
                new IncidentDetector(90.0,
                        new AvailabilityValidator(new DurationAndHttpStatusRule(45)),
                        capture),
                new AvailabilityValidator(new DurationAndHttpStatusRule(45))
        );

        try (ByteArrayInputStream in =
                     new ByteArrayInputStream(LINES.getBytes(StandardCharsets.UTF_8))) {
            p.process(in);
        }

        assertEquals(1, incidents.get());
    }
}

package org.gardar.analyzer.detector;

import org.gardar.analyzer.domain.Incident;
import org.gardar.analyzer.domain.OneSecondStats;
import org.gardar.analyzer.report.IncidentReporter;
import org.gardar.analyzer.validator.AvailabilityValidator;
import org.gardar.analyzer.validator.rule.DurationAndHttpStatusRule;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IncidentDetectorTest {

    @Test
    void detectsSimpleIncident() {
        List<Incident> captured = new ArrayList<>();
        IncidentReporter stub = captured::add;

        AvailabilityValidator validator =
                new AvailabilityValidator(new DurationAndHttpStatusRule(1000));
        IncidentDetector det = new IncidentDetector(90.0, validator, stub);

        // second 0  → 2/3 ok = 66%  → incident start
        LocalDateTime t0 = LocalDateTime.of(2025, 5, 24, 10, 0, 0);
        OneSecondStats stats = new OneSecondStats();
        stats.record(true);
        stats.record(false);
        stats.record(true);
        det.detectIncident(t0, stats);

        // second 1  → 1/1 ok = 100% → incident ends
        LocalDateTime t1 = t0.plusSeconds(1);
        OneSecondStats s1 = new OneSecondStats();
        s1.record(true);
        det.detectIncident(t1, s1);

        det.finish();

        assertEquals(1, captured.size());
        Incident inc = captured.getFirst();
        assertEquals(t0, inc.start());
        assertEquals(t0, inc.end());                // закончился на предыдущей секунде
        assertEquals(66.666, inc.availabilityPercent(), 0.01);
    }
}

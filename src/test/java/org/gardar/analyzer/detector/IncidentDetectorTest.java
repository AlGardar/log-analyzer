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
        List<Incident> incidents = new ArrayList<>();
        IncidentReporter mockReport = incidents::add;

        var validator = new AvailabilityValidator(new DurationAndHttpStatusRule(45));
        var det = new IncidentDetector(90.0, validator, mockReport);

        // second 0  → 2/3 ok = 66%  → incident start
        LocalDateTime t0 = LocalDateTime.of(2025, 5, 24, 10, 0, 0);
        OneSecondStats s0 = new OneSecondStats();
        s0.record(true);
        s0.record(false);
        s0.record(true);
        det.detectIncident(t0, s0);

        // second 1  → 1/1 ok = 0% → incident continue
        LocalDateTime t1 = t0.plusSeconds(1);
        OneSecondStats s1 = new OneSecondStats();
        s1.record(false);
        det.detectIncident(t1, s1);

        // second 2  → 1/1 ok = 100% → incident end
        LocalDateTime t2 = t1.plusSeconds(1);
        OneSecondStats s2 = new OneSecondStats();
        s2.record(true);
        det.detectIncident(t2, s2);

        det.finish();

        assertEquals(1, incidents.size());
        Incident inc1 = incidents.get(0);
        assertEquals(t0, inc1.start());
        assertEquals(t1, inc1.end());
        assertEquals(50, inc1.availabilityPercent(), 0.01);
    }
}

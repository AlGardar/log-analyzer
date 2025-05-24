package org.gardar.analyzer.detector;

import lombok.RequiredArgsConstructor;
import org.gardar.analyzer.domain.Incident;
import org.gardar.analyzer.domain.OneSecondStats;
import org.gardar.analyzer.report.IncidentReporter;
import org.gardar.analyzer.validator.AvailabilityValidator;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class IncidentDetector {
    private final double availabilityThresholdPercent;
    private final AvailabilityValidator validator;
    private final IncidentReporter reporter;

    private boolean inIncident;
    private LocalDateTime started;
    private int sumTotalRequests;
    private int sumSuccessRequests;
    private LocalDateTime lastSecond;


    public void detectIncident(LocalDateTime currentSecond, OneSecondStats stats) {
        double currentAvailabilityPercent = validator.availabilityPercent(stats);

        if (currentAvailabilityPercent < availabilityThresholdPercent) {
            if (!inIncident) {
                inIncident = true;
                started = currentSecond;
                sumTotalRequests = stats.getTotalRequests();
                sumSuccessRequests = stats.getSuccessRequests();
            } else {
                sumTotalRequests += stats.getTotalRequests();
                sumSuccessRequests += stats.getSuccessRequests();
            }
        } else if (inIncident) {
            closeIncident(currentSecond.minusSeconds(1));
        }
        lastSecond = currentSecond;
    }


    public void finish() {
        if (inIncident) closeIncident(lastSecond);
    }

    private void closeIncident(LocalDateTime end) {
        double combined = 100.0 * sumSuccessRequests / sumTotalRequests;
        reporter.report(new Incident(started, end, combined));
        inIncident = false;
    }
}

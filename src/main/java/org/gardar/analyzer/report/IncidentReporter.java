package org.gardar.analyzer.report;

import org.gardar.analyzer.domain.Incident;

public interface IncidentReporter {
    void report(Incident incident);
}
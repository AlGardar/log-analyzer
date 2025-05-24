package org.gardar.analyzer.report;

import org.gardar.analyzer.domain.Incident;

public class ConsoleIncidentReporter implements IncidentReporter {
    @Override
    public void report(Incident incident) {
        System.out.println(incident);
    }
}

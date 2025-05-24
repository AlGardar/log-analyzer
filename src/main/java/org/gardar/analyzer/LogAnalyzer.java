package org.gardar.analyzer;

import org.gardar.analyzer.config.Config;
import org.gardar.analyzer.detector.IncidentDetector;
import org.gardar.analyzer.parser.DefaultLogParser;
import org.gardar.analyzer.processor.StreamingLogProcessor;
import org.gardar.analyzer.report.ConsoleIncidentReporter;
import org.gardar.analyzer.report.IncidentReporter;
import org.gardar.analyzer.validator.AvailabilityValidator;
import org.gardar.analyzer.validator.rule.AvailabilityRule;
import org.gardar.analyzer.validator.rule.DurationAndHttpStatusRule;

public class LogAnalyzer {

    public static void main(String[] args) throws Exception {
        Config cfg = Config.fromArgs(args);

        DefaultLogParser parser = new DefaultLogParser();

        AvailabilityRule rule = new DurationAndHttpStatusRule(cfg.getDurationThresholdMs());
        AvailabilityValidator validator = new AvailabilityValidator(rule);
        IncidentReporter consoleReporter = new ConsoleIncidentReporter();
        IncidentDetector detector = new IncidentDetector(cfg.getAvailabilityThresholdPercent(), validator, consoleReporter);

        StreamingLogProcessor processor = new StreamingLogProcessor(parser, detector, validator);
        processor.process(System.in);
    }
}

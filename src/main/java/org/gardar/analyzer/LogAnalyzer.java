package org.gardar.analyzer;

import org.gardar.analyzer.config.Config;
import org.gardar.analyzer.parser.LogParser;

public class LogAnalyzer {

    public static void main(String[] args) {
        Config cfg = Config.fromArgs(args);

        LogParser parser = new LogParser();
    }
}

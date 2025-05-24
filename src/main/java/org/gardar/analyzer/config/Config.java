package org.gardar.analyzer.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Config {

    private final double availabilityThreshold;
    private final double timeThreshold;

    public static Config fromArgs(String[] args) {
        double avail = 100.0;
        double time = Double.MAX_VALUE;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-u" -> {
                    if (i + 1 >= args.length) {
                        throw new IllegalArgumentException("Expected a value after -u");
                    }
                    avail = Double.parseDouble(args[++i]);
                }

                case "-t" -> {
                    if (i + 1 >= args.length) {
                        throw new IllegalArgumentException("Expected a value after -t");
                    }
                    time = Double.parseDouble(args[++i]);
                }

                default -> throw new IllegalArgumentException("Unknown argument: " + args[i]);
            }
        }
        return new Config(avail, time);
    }
}

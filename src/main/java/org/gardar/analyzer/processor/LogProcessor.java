package org.gardar.analyzer.processor;

import java.io.IOException;
import java.io.InputStream;

public interface LogProcessor {
    void process(InputStream in) throws IOException;
}

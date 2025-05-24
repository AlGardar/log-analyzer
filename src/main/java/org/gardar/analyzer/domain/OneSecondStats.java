package org.gardar.analyzer.domain;

import lombok.Getter;

@Getter
public class OneSecondStats {
    private int totalRequests;
    private int successRequests;

    public void record(boolean isSuccess) {
        totalRequests++;
        if (isSuccess) successRequests++;
    }

    public void reset() {
        totalRequests = 0;
        successRequests = 0;
    }
}
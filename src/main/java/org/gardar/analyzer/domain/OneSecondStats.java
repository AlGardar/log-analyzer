package org.gardar.analyzer.domain;

import lombok.Getter;

@Getter
public class OneSecondStats {
    private int oneSecondTotalRequests;
    private int oneSecondSuccessRequests;

    public void record(boolean isSuccess) {
        oneSecondTotalRequests++;
        if (isSuccess) oneSecondSuccessRequests++;
    }

    public void reset() {
        oneSecondTotalRequests = 0;
        oneSecondSuccessRequests = 0;
    }
}
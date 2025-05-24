package org.gardar.analyzer.validator;

import org.gardar.analyzer.domain.OneSecondStats;
import org.gardar.analyzer.validator.rule.DurationAndHttpStatusRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AvailabilityValidatorTest {

    @Test
    void percentCalculation() {
        AvailabilityValidator v = new AvailabilityValidator(new DurationAndHttpStatusRule(1));
        OneSecondStats s = new OneSecondStats();
        s.record(true);  // ok
        s.record(false); // fail
        s.record(true);  // ok
        assertEquals(66.666, v.availabilityPercent(s), 0.01);
    }

    @Test
    void percentWhenNoRequests() {
        AvailabilityValidator v = new AvailabilityValidator(new DurationAndHttpStatusRule(1));
        OneSecondStats empty = new OneSecondStats();
        assertEquals(100.0, v.availabilityPercent(empty));
    }
}

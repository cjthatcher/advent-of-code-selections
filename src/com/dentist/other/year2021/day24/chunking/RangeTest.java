package com.dentist.other.year2021.day24.chunking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RangeTest {

    @Test
    public void testNoSplit() {
        Range r = new Range(0, 20);
        Range[] parts = r.splitTo(10000);
        Assertions.assertEquals(1, parts.length);
        Assertions.assertEquals(0, parts[0].start());
        Assertions.assertEquals(20, parts[0].end());
    }

}

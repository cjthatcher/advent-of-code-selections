package com.dentist.other.year2021.day19;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BeaconTest {

    @Test
    public void parseBeacon() {
        Beacon b = Beacon.fromString("404,-588,-901", "0", 0);

        Assertions.assertEquals("0:0", b.name);
        Assertions.assertEquals(new Coordinates3d(404, -588, -901), b.coordinates);
    }
}

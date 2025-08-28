package com.dentist.other.year2021.day19;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CoordinatesTest {

    @Test
    public void parseCoordinates() {
        Assertions.assertEquals(new Coordinates3d(1, 33, 7), Coordinates3d.fromString("1,33,7"));
    }

    @Test
    public void distance() {
        Coordinates3d a = new Coordinates3d(0,0,0);
        Coordinates3d b = new Coordinates3d(1, 0,0);
        Assertions.assertEquals(1, a.distance(b), 0.00005);
    }

    @Test
    public void distance2() {
        Coordinates3d a = new Coordinates3d(0,0,0);
        Coordinates3d b = new Coordinates3d(1, 1,0);
        Assertions.assertEquals(Math.sqrt(2), a.distance(b), 0.00005);
    }

    @Test
    public void distance3() {
        Coordinates3d a = new Coordinates3d(0,0,0);
        Coordinates3d b = new Coordinates3d(1, 1,1);
        Assertions.assertEquals(Math.sqrt(3), a.distance(b), 0.00005);
    }

    @Test
    public void distance4() {
        Coordinates3d a = new Coordinates3d(1,-100,-4);
        Coordinates3d b = new Coordinates3d(0, 10,-7);
        // 1 + 110^2 + 9 = 1 + 12100 + 9 = 12110
        // sqrt 12110 =
        Assertions.assertEquals(Math.sqrt(12110), a.distance(b), 0.00005);
    }
}

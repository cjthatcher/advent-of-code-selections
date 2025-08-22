package com.dentist.other.year2021.day22.part2.cuboids;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class CuboidTest {

    @Test
    public void HollowTest() {
        Cuboid orig = new Cuboid(true, 0, 3, 0, 3, 0, 3);
        Assertions.assertEquals(27, orig.volume());

        Cuboid middle = new Cuboid(false, 1, 2, 1, 2, 1, 2);
        Assertions.assertEquals(1, middle.volume());

        Set<Cuboid> remainder = orig.fragmentAround(middle);

        // sum of volumes of remainder should be 26
        long volume = remainder.stream().mapToLong(Cuboid::volume).sum();
        Assertions.assertEquals(26, volume);
    }

    @Test
    public void DivotTest() {
        Cuboid orig = new Cuboid(true, 0, 3, 0, 3, 0, 3);
        Assertions.assertEquals(27, orig.volume());

        Cuboid middle = new Cuboid(false, 1, 2, 1, 2, 0, 2);
        Assertions.assertEquals(2, middle.volume());

        Set<Cuboid> remainder = orig.fragmentAround(middle);

        long volume = remainder.stream().mapToLong(Cuboid::volume).sum();
        Assertions.assertEquals(25, volume);
    }

    @Test
    public void TunnelTest() {
        Cuboid orig = new Cuboid(true, 0, 3, 0, 3, 0, 3);
        Assertions.assertEquals(27, orig.volume());

        Cuboid middle = new Cuboid(false, 1, 2, -1000, 1000, 1, 2);
        Assertions.assertEquals(2000, middle.volume());

        Set<Cuboid> remainder = orig.fragmentAround(middle);

        long volume = remainder.stream().mapToLong(Cuboid::volume).sum();
        Assertions.assertEquals(24, volume);
    }

    @Test
    public void SideTest() {
        Cuboid orig = new Cuboid(true, 0, 3, 0, 3, 0, 3);
        Assertions.assertEquals(27, orig.volume());

        Cuboid interloper = new Cuboid(false, 1, 5, 0, 3, 0, 3);
        Assertions.assertEquals(36, interloper.volume());

        Set<Cuboid> remainder = orig.fragmentAround(interloper);

        long volume = remainder.stream().mapToLong(Cuboid::volume).sum();
        Assertions.assertEquals(9, volume);
    }

    @Test
    public void AlmostCorner() {
        Cuboid orig = new Cuboid(true, 0, 3, 0, 3, 0, 3);
        Assertions.assertEquals(27, orig.volume());

        Cuboid interloper = new Cuboid(false, 1, 5, 2, 5, 0, 2);
        Assertions.assertEquals(24, interloper.volume());

        Set<Cuboid> remainder = orig.fragmentAround(interloper);

        long volume = remainder.stream().mapToLong(Cuboid::volume).sum();
        Assertions.assertEquals(23, volume);
    }

    @Test
    public void Bifurcate() {
        Cuboid orig = new Cuboid(true, 0, 3, 0, 3, 0, 3);
        Assertions.assertEquals(27, orig.volume());

        Cuboid interloper = new Cuboid(false, -10, 10, 1, 2, -10, 10);
        Assertions.assertEquals(400, interloper.volume());

        Set<Cuboid> remainder = orig.fragmentAround(interloper);

        long volume = remainder.stream().mapToLong(Cuboid::volume).sum();
        Assertions.assertEquals(18, volume);
        Assertions.assertEquals(2, remainder.size());
    }

    @Test
    public void TotalEclipseOfTheHeart() {
        Cuboid orig = new Cuboid(true, 0, 3, 0, 3, 0, 3);
        Assertions.assertEquals(27, orig.volume());

        Cuboid interloper = new Cuboid(false, -10, 10, -10, 10, -10, 10);
        Assertions.assertEquals(8000, interloper.volume());

        Set<Cuboid> remainder = orig.fragmentAround(interloper);

        // sum of volumes of remainder should be 26
        long volume = remainder.stream().mapToLong(Cuboid::volume).sum();
        Assertions.assertEquals(0, volume);
    }

    @Test
    public void moderateSawInjury() {
        Cuboid orig = new Cuboid(true, 0, 3, 0, 3, 0, 3);
        Assertions.assertEquals(27, orig.volume());

        Cuboid interloper = new Cuboid(false, 1, 5, 1, 2, 0, 5);
        Assertions.assertEquals(20, interloper.volume());

        Set<Cuboid> remainder = orig.fragmentAround(interloper);

        long volume = remainder.stream().mapToLong(Cuboid::volume).sum();
        Assertions.assertEquals(21, volume);
    }

    @Test
    public void corner() {
        Cuboid orig = new Cuboid(true, 0, 3, 0, 3, 0, 3);
        Assertions.assertEquals(27, orig.volume());

        Cuboid interloper = new Cuboid(false, 1, 5, 1, 5, 0, 5);
        Assertions.assertEquals(80, interloper.volume());

        Set<Cuboid> remainder = orig.fragmentAround(interloper);

        long volume = remainder.stream().mapToLong(Cuboid::volume).sum();
        Assertions.assertEquals(15, volume);
    }
}

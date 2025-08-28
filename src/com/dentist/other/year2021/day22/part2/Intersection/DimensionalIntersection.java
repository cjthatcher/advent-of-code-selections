package com.dentist.other.year2021.day22.part2.Intersection;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public record DimensionalIntersection(Orientation orientation, Set<Range> rangesToFill){

    public static DimensionalIntersection generateIntersection(int theirA, int theirB, int myA, int myB) {
        if (theirB <= myA || theirA >= myB) {
            return new DimensionalIntersection(Orientation.NONE, Collections.emptySet());
        }

        // Inner
        if (isInnerIntersection(theirA, theirB, myA, myB)) {
            return innerIntersectionRanges(theirA, theirB, myA, myB);
        }

        // Open Positive
        if (isOpenPositive(theirA, theirB, myA, myB)) {
            return openPositiveRanges(theirA, myA, myB);
        }

        // Open Negative
        if (isOpenNegative(theirA, theirB, myA, myB)) {
            return openNegativeRanges(theirB, myA, myB);
        }

        // Eclipse
        if (isEclipse(theirA, theirB, myA, myB)) {
            return eclipseRanges(myA, myB);
        }

        throw new RuntimeException("Chris sucks at math this should not have happened.");
    }

    // If someone else is alive, then you are the whole range. Otherwise you are nothing.
    private static DimensionalIntersection eclipseRanges(int myA, int myB) {
        Set<Range> ranges = new HashSet<>();
        ranges.add(new Range(myA, myB, Liveliness.ALIVE_FRIENDS));
        return new DimensionalIntersection(Orientation.ECLIPSE, ranges);
    }

    // You are alive for the first segment, trying to be dead for the middle segment, and alive for the last segment.
    // If others force you to be alive for the middle segment, you are alive there too.
    private static DimensionalIntersection innerIntersectionRanges(int theirA, int theirB, int myA, int myB) {
        Set<Range> ranges = new HashSet<>();
        ranges.add(new Range(theirA, theirB, Liveliness.WANTS_TO_BE_DEAD));
        ranges.add(new Range(myA, theirA, Liveliness.ALIVE));
        ranges.add(new Range(theirB, myB, Liveliness.ALIVE));
        return new DimensionalIntersection(Orientation.INNER, ranges);
    }

    // You are alive on the bottom. If someone else is alive, you're also alive in the middle to the end.
    private static DimensionalIntersection openPositiveRanges(int theirA, int myA, int myB) {
        Set<Range> ranges = new HashSet<>();
        ranges.add(new Range(theirA, myB, Liveliness.ALIVE_FRIENDS)); // If everyone else is alive
        ranges.add(new Range(myA, theirA, Liveliness.ALIVE)); // Stubbornly Alive
        return new DimensionalIntersection(Orientation.OPEN_POSITIVE, ranges);
    }

    // You are alive on the top. If someone else is alive, you are also alive from the middle to the bottom.
    private static DimensionalIntersection openNegativeRanges(int theirB, int myA, int myB) {
        Set<Range> ranges = new HashSet<>();
        ranges.add(new Range(myA, theirB, Liveliness.ALIVE_FRIENDS)); // if everyone else is alive
        ranges.add(new Range(theirB, myB, Liveliness.ALIVE));
        return new DimensionalIntersection(Orientation.OPEN_NEGATIVE, ranges);
    }

    private static boolean isEclipse(int theirA, int theirB, int myA, int myB) {
        return theirA <= myA && theirB >= myB;
    }

    private static boolean isOpenNegative(int theirA, int theirB, int myA, int myB) {
        return theirA <= myA && theirB > myA && theirB < myB;
    }

    private static boolean isOpenPositive(int theirA, int theirB, int myA, int myB) {
        return theirA > myA && theirA < myB && theirB >= myB;
    }

    private static boolean isInnerIntersection(int theirA, int theirB, int myA, int myB) {
        return theirA > myA && theirB < myB;
    }

}



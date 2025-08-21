package com.dentist.other.year2021.day22.cubes;

import java.util.HashSet;
import java.util.Set;

public class Cuboid {

    private final int x1, x2, y1, y2, z1, z2;
    private final boolean on;

    public Cuboid(boolean on, int x1, int x2, int y1, int y2, int z1, int z2) {
        this.on = on;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.z1 = z1;
        this.z2 = z2;
    }

    boolean intersects(Cuboid o) {
        // Their RIGHT is greater than my left, and their LEFT is less than my right.
        boolean xIntersects = o.x2 > x1 && o.x1 < x2;
        boolean yIntersects = o.y2 > y1 && o.y1 < y2;
        boolean zIntersects = o.z2 > z1 && o.z1 < z2;

        return xIntersects && yIntersects && zIntersects;
    }

    private Intersection oneDimensionalIntersection(int theirA, int theirB, int myA, int myB) {
        if (theirB < myA || theirA > myB) {
            return Intersection.NONE;
        }

        if (theirA > myA && theirB < myB) {
            return Intersection.INNER;
        }

        if (theirA > myA && theirA < myB && theirB > myB) {
            return Intersection.OPEN_POSITIVE;
        }

        if (theirA < myA && theirB > myA && theirB < myB) {
            return Intersection.OPEN_NEGATIVE;
        }

        if (theirA < myA && theirB > myB) {
            return Intersection.ECLIPSE;
        }

        throw new RuntimeException("Chris sucks at math this should not have happened.");
    }

    Intersection xIntersection(int theirXa, int theirXb) {
        return oneDimensionalIntersection(theirXa, theirXb, x1, x2);
    }

    Intersection yIntersection(int theirYa, int theirYb) {
        return oneDimensionalIntersection(theirYa, theirYb, y1, y2);
    }

    Intersection zIntersection(int theirZa, int theirZb) {
        return oneDimensionalIntersection(theirZa, theirZb, z1, z2);
    }

    /**
     * Given a Cuboid that intersects with this Cuboid, return a set of fragmented Cuboids that
     * DO NOT intersect with the interloper, but cover *all* the uninterloped volume of the original cuboid.
     */
    public Set<Cuboid> fragmentAround(Cuboid interloper) {
        Set<Cuboid> fragments = new HashSet<>();

        // If there is no intersection, then this original cuboid is sufficiently fragmented.
        if (!intersects(interloper)) {
            fragments.add(this);
            return fragments;
        }

        // There are 10 unique interloping patterns.
        // 0 - Inside
        //    --> x inside, y inside, z inside.
        //    --> Need a big Cuboid on top, on bottom, then four to fill in the middle. Two biggies, then two skinnies.
        //    --> Will end up with six cuboids. But if we didn't cheat and make big guys, we could do 9 on top, 8 in mid, 9 on bot for 27-1. Maybe maybe maybe.
        //    --> Inside means we'll need *three* segmentations along that dimension, with a *skip* in the middle.

        // 1 - Divot
        //    --> x inside, y inside, z open negative
        //    --> We could get this done in four cuboids. // Right side, Left side, top middle, bottom middle.
        //    --> So what we're going to end up with, always, is 27 sub-cuboids, based on the intersection points, and we black out the ones we can't fill. Of course we could simplify that if we wanted to.

        // 2 - Mild Saw Injury
        //    --> x open positive, y inside, z open negative
        //    --> Missing four adjacent cubes from the middle layer.
        //    XXX   XXX   XXX
        //    XOO   XOO   XXX
        //    XXX   XXX   XXX

        // 2 - Tunnel
        //   --> x inside, y inside, z eclipse (open open)
        //   XXX  XXX  XXX
        //   XOX  XOX  XOX
        //   XXX  XXX  XXX
        // 3 - Almost Corner
        // 3 - Moderate Saw Injury
        // 4 - Corner (3d Utah)
        // 4 - Bifurcate (Severe saw injury)
        // 5 - Side Gone
        // 6 - Eclipse

        // And, most of them have rotations. So, how do I write this code in a way that doesn't suck _terribly_?


    }


}

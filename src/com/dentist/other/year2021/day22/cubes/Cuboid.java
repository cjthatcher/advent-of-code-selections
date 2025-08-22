package com.dentist.other.year2021.day22.cubes;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    private static Optional<Cuboid> fromRanges(Range rx, Range ry, Range rz, boolean on) {
        // Alive
        // Dead
        // Alive Friends
        // Dead Friends

        if (ry.a1() == 1 && ry.a2() == 2) {
//            System.out.println("f you");
        }
        EnumSet<Liveliness> livelinesses = EnumSet.of(rx.liveliness(), ry.liveliness(), rz.liveliness());

        if (livelinesses.contains(Liveliness.ALIVE) && livelinesses.contains(Liveliness.DEAD_FRIENDS)) {
            return Optional.empty();
        }

        if (livelinesses.contains(Liveliness.ALIVE_FRIENDS) && !livelinesses.contains(Liveliness.ALIVE)) {
            return Optional.empty();
        }


//        if (livelinesses.contains(Liveliness.WANTS_TO_BE_DEAD) && livelinesses.contains(Liveliness.ALIVE_FRIENDS)) {
//            return Optional.empty();
//        }

        if (rx.liveliness() == ry.liveliness() &&
                ry.liveliness() == rz.liveliness() &&
                rz.liveliness() == Liveliness.WANTS_TO_BE_DEAD) {
            return Optional.empty();
        }

        Cuboid c = new Cuboid(on, rx.a1(), rx.a2(), ry.a1(), ry.a2(), rz.a1(), rz.a2());
        System.out.printf("Creating %d,%d;  %d,%d;  %d,%d. Liveliness was %s, %s, %s, volume = %d\r\n", rx.a1(), rx.a2(), ry.a1(), ry.a2(), rz.a1(), rz.a2(), rx.liveliness(), ry.liveliness(), rz.liveliness(), c.volume());
        return Optional.of(c);
    }

    public long volume() {
        return (long) Math.abs(x2 - x1) * (long) Math.abs(y2 - y1) * (long) Math.abs(z2 - z1);
    }

    boolean intersects(Cuboid o) {
        // Their RIGHT is greater than my left, and their LEFT is less than my right.
        boolean xIntersects = o.x2 > x1 && o.x1 < x2;
        boolean yIntersects = o.y2 > y1 && o.y1 < y2;
        boolean zIntersects = o.z2 > z1 && o.z1 < z2;

        return xIntersects && yIntersects && zIntersects;
    }

    private DimensionalIntersection oneDimensionalIntersection(int theirA, int theirB, int myA, int myB) {
        Set<Range> ranges = new HashSet<>();

        if (theirB <= myA || theirA >= myB) {
            return new DimensionalIntersection(Intersection.NONE, ranges);
        }

        // Inner
        if (theirA > myA && theirB < myB) {
//            ranges.add(new Range(myA, myB, Liveliness.ALIVE_FRIENDS));
            ranges.add(new Range(theirA, theirB, Liveliness.WANTS_TO_BE_DEAD));
            ranges.add(new Range(myA, theirA, Liveliness.ALIVE));
            ranges.add(new Range(theirB, myB, Liveliness.ALIVE));
            return new DimensionalIntersection(Intersection.INNER, ranges);
        }

        // Open Positive
        if (theirA > myA && theirA < myB && theirB >= myB) {
            ranges.add(new Range(theirA, myB, Liveliness.ALIVE_FRIENDS)); // If everyone else is alive
            ranges.add(new Range(myA, theirA, Liveliness.ALIVE)); // Stubbornly Alive
//            ranges.add(new Range(theirB, myB, Liveliness.WANTS_TO_BE_DEAD)); // Try not to fill this?
            return new DimensionalIntersection(Intersection.OPEN_POSITIVE, ranges);
        }

        // Open Negative
        if (theirA <= myA && theirB > myA && theirB < myB) {
            ranges.add(new Range(myA, theirB, Liveliness.ALIVE_FRIENDS)); // if everyone else is alive
            ranges.add(new Range(theirB, myB, Liveliness.ALIVE));
//            ranges.add(new Range(myA, theirA, Liveliness.WANTS_TO_BE_DEAD));
            return new DimensionalIntersection(Intersection.OPEN_NEGATIVE, ranges);
        }

        // Eclipse
        if (theirA <= myA && theirB >= myB) {
            ranges.add(new Range(myA, myB, Liveliness.ALIVE_FRIENDS));
            return new DimensionalIntersection(Intersection.ECLIPSE, ranges);
        }

        throw new RuntimeException("Chris sucks at math this should not have happened.");
    }

    DimensionalIntersection xIntersection(int theirXa, int theirXb) {
        return oneDimensionalIntersection(theirXa, theirXb, x1, x2);
    }

    DimensionalIntersection yIntersection(int theirYa, int theirYb) {
        return oneDimensionalIntersection(theirYa, theirYb, y1, y2);
    }

    DimensionalIntersection zIntersection(int theirZa, int theirZb) {
        return oneDimensionalIntersection(theirZa, theirZb, z1, z2);
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
    //    Front  Mid    Back
    //    XXX    XXX    XXX
    //    XOO    XOO    XXX
    //    XXX    XXX    XXX

    //   (top left front) -->

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

        DimensionalIntersection xInter = xIntersection(interloper.x1, interloper.x2);
        DimensionalIntersection yInter = yIntersection(interloper.y1, interloper.y2);
        DimensionalIntersection zInter = zIntersection(interloper.z1, interloper.z2);

        // if all three dimensions are an eclipse, then we can replace the current cuboid entirely with the interloper.
        if (xInter.orientation() == Intersection.ECLIPSE
                && yInter.orientation() == Intersection.ECLIPSE &&
                zInter.orientation() == Intersection.ECLIPSE) {
            return fragments;
        }

        // Otherwise, we will create cuboids to represent the uninterloped segments.

        // furthest left --> xa of the original cube
        // mid left --> xa of the interloper (if closed / inside on left). If open / eclipse then still xa of original.
        // mid right --> xb of the interloper (if closed / inside on right). If open / eclipse then xb of orig.
        // furthest right --> xb of the original

        int furthestLeft = this.x1;
        int midLeft = Math.max(this.x1, interloper.x1);
        int midRight = Math.min(interloper.x2, this.x2);
        int furthestRight = this.x2;

        int[] x = new int[]{furthestLeft, midLeft, midRight, furthestRight};

        int lowest = this.y1;
        int midLow = Math.max(this.y1, interloper.y1);
        int midHigh = Math.min(interloper.y2, this.y2);
        int highest = this.y2;

        int[] y = new int[]{lowest, midLow, midHigh, highest};

        int shallow = this.z1;
        int midShallow = Math.max(this.z1, interloper.z1);
        int midDeep = Math.min(interloper.z2, this.z2);
        int deep = this.z2;

        int[] z = new int[]{shallow, midShallow, midDeep, deep};


        // Going to create 27 cuboids. Some of them will be empty.


        // How many different x depths will we need to see?

        // if x is eclipse, there's only one real size --> the original width. It's that or zero.

        // if x is one side open, we might have to be full size, and we might have to be half size. Never zero.

        // if x is two sides closed, we might have to be full size, might have to be from wall to a, and then from b to wall.
        Set<Cuboid> smallCuboids = new HashSet<>();

        for (Range rx : xInter.rangesToFill()) {
            for (Range ry : yInter.rangesToFill()) {
                for (Range rz : zInter.rangesToFill()) {
                    Cuboid.fromRanges(rx, ry, rz, this.on).ifPresent(smallCuboids::add);
                }
            }
        }

//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                for (int k = 0; k < 3; k++) {
//                    boolean canSkipX = false;
//                    if (i == 0) canSkipX = xInter.openNegative;
//                    if (i == 1) canSkipX = true;
//                    if (i == 2) canSkipX = xInter.openPositive;
//
//                    boolean canSkipY = false;
//                    if (j == 0) canSkipY = yInter.openNegative;
//                    if (j == 1) canSkipY = true;
//                    if (j == 2) canSkipY = yInter.openPositive;
//
//                    boolean canSkipZ = false;
//                    if (k == 0) canSkipZ = zInter.openNegative;
//                    if (k == 1) canSkipZ = true;
//                    if (k == 2) canSkipZ = zInter.openPositive;
//
//                    if (canSkipX && canSkipY && canSkipZ) {
//                        // don't need to add a cube here.
//                        continue;
//                    } else {
//                        Cuboid fragment = new Cuboid(this.on, x[i], x[i] + 1, y[j], y[j+1], z[k], z[k+1]);
//                        if (fragment.volume() > 0) {
//                            smallCuboids.add(fragment);
//                        }
//                    }
//                }
//            }
//        }

        return smallCuboids;
    }


}

package com.dentist.other.year2021.day19;

import java.util.Objects;

public class Coordinates3d {

    int x, y, z;

    Coordinates3d(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Coordinates3d fromString(String input) {
        //("1,33,7")
        String[] parts = input.split(",");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        int z = Integer.parseInt(parts[2]);
        return new Coordinates3d(x, y, z);
    }

    public static long manhattanDistance(Coordinates3d a, Coordinates3d b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y) + Math.abs(a.z - b.z);
    }

    public Coordinates3d delta(Coordinates3d c) {
        return new Coordinates3d(x - c.x, y - c.y, z - c.z);
    }

    public double magnitude() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    public double distance(Coordinates3d c) {
        return delta(c).magnitude();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates3d that = (Coordinates3d) o;
        return x == that.x && y == that.y && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return String.format("<%d,%d,%d>", x, y, z);
    }

//    public Coordinates3d applyTransformation(Scanner.Transformation t) {
//
//    }
//
//    public Coordinates3d applyRotation(Rotation r) {
//
//    }

    public Coordinates3d add(Coordinates3d vector) {
        return new Coordinates3d(x + vector.x, y + vector.y, z + vector.z);
    }

    /** This vector is sufficiently interesting, so we can use it to figure out how we need to rotate things. */
    public boolean isGoodBasis() {
        return x != 0 && y != 0 && z != 0
                && x != y
                && x != z
                && y != z;
    }
}



package com.dentist.other.year2021.day19;

import java.util.Objects;
import java.util.Set;

public class Beacon {

    Coordinates3d coordinates;
    String name;

    public static Beacon fromString(String input, String parent, int index) {
        Coordinates3d coordinates = Coordinates3d.fromString(input);
        return new Beacon(coordinates,  parent + ":" + index);
    }

    public Beacon(Coordinates3d coordinates, String name) {
        this.coordinates = coordinates;
        this.name = name;
    }

    public Beacon transform(Scanner.Transformation t) {
        Coordinates3d c = t.r().apply(coordinates).add(t.rehomeVector());
        return new Beacon(c, name + "_rotated");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Beacon beacon = (Beacon) o;
        return Objects.equals(coordinates, beacon.coordinates) && Objects.equals(name, beacon.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates, name);
    }

    @Override
    public String toString() {
        return String.format("%s, %s", name, coordinates);
    }

}


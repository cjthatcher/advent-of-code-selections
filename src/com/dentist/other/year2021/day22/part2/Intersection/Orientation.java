package com.dentist.other.year2021.day22.part2.Intersection;

public enum Orientation {

    NONE(false,false),
    ECLIPSE(true,true),
    INNER(false,false),
    OPEN_POSITIVE(false,true),
    OPEN_NEGATIVE(true,false);

    private Orientation(boolean openNegative, boolean openPositive) {
        this.openNegative = openNegative;
        this.openPositive = openPositive;
    }
    final boolean openPositive;

    final boolean openNegative;

    public boolean closedNegative() {
        return !openNegative;
    }

    public boolean closedPositive() {
        return !openPositive;
    }

}

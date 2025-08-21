package com.dentist.other.year2021.day22.cubes;

public enum Intersection {

    NONE(false,false),
    ECLIPSE(true,true),
    INNER(false,false),
    OPEN_POSITIVE(false,true),
    OPEN_NEGATIVE(true,false);

    private Intersection(boolean openNegative, boolean openPositive) {
        this.openNegative = openNegative;
        this.openPositive = openPositive;
    }
    final boolean openPositive;

    final boolean openNegative;

}

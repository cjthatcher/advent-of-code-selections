package com.dentist.other.year2021.day25;

import com.dentist.other.year2021.day23.Coordinate;

public enum SeaSpace {
    DOWN(new Coordinate(1, 0)),
    RIGHT(new Coordinate(0, 1)),
    RIGHT_EMPTY(new Coordinate(0, 0)),
    EMPTY(new Coordinate(0, 0));

    Coordinate target;

    SeaSpace(Coordinate target) {
        this.target = target;
    }
}

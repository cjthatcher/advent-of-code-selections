package com.dentist.other.year2021.day23;

import java.util.Set;

public record Coordinate(int row, int column) {

    public Coordinate up() {
        return new Coordinate(row -1, column);
    }

    public Coordinate down() {
        return new Coordinate(row+1, column);
    }

    public Coordinate right() {
        return new Coordinate(row, column + 1);
    }

    public Coordinate left() {
        return new Coordinate(row, column -1 );
    }

    public Coordinate upRight() {
        return new Coordinate(row -1, column + 1);
    }

    public Coordinate upLeft() {
        return new Coordinate(row -1, column -1);
    }

    public Coordinate downRight() {
        return new Coordinate(row + 1, column +1);
    }

    public Coordinate downLeft() {
        return new Coordinate(row + 1, column -1);
    }

    public Coordinate add(Coordinate other) {
        return new Coordinate(row + other.row, column + other.column);
    }

    public static final Coordinate UP = new Coordinate(-1, 0);
    public static final Coordinate DOWN = new Coordinate(1, 0);
    public static final Coordinate LEFT = new Coordinate(0, -1);
    public static final Coordinate RIGHT = new Coordinate(0, 1);
    public static final Coordinate UP_RIGHT = new Coordinate(-1, 1);
    public static final Coordinate UP_LEFT = new Coordinate(-1, -1);
    public static final Coordinate DOWN_RIGHT = new Coordinate(1, 1);
    public static final Coordinate DOWN_LEFT = new Coordinate(1, -1);
    public static final Coordinate STAY = new Coordinate(0, 0);

    public static final Set<Coordinate> directions = Set.of(UP, DOWN, LEFT, RIGHT, UP_RIGHT, UP_LEFT, DOWN_RIGHT, DOWN_LEFT);
}


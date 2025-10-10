package com.dentist.other.year2021.day23;

public enum CardinalDirection {

    UP(Coordinate.UP),
    DOWN(Coordinate.DOWN),
    LEFT(Coordinate.LEFT),
    RIGHT(Coordinate.RIGHT);

    public final Coordinate movement;

    private CardinalDirection(Coordinate movement) {
        this.movement = movement;
    }

    public CardinalDirection turnRight() {
        return switch (this) {
            case UP -> RIGHT;
            case RIGHT -> DOWN;
            case DOWN -> LEFT;
            case LEFT -> UP;
        };
    }
}

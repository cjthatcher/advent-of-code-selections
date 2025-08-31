package com.dentist.other.year2021.day25;

public record Coordinate(int row, int column) {

    Coordinate add(Coordinate other) {
        return new Coordinate(row + other.row, column + other.column);
    }
}

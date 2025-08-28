package com.dentist.other.year2021.day20.input;

public record RowColumn(int row, int column) {

    public RowColumn topLeft() {
        return new RowColumn(row - 1, column - 1);
    }

    public RowColumn top() {
        return new RowColumn(row - 1, column);
    }

    public RowColumn topRight() {
        return new RowColumn(row - 1, column + 1);
    }

    public RowColumn left() {
        return new RowColumn(row, column - 1);
    }

    public RowColumn center() {
        return new RowColumn(row, column);
    }

    public RowColumn right() {
        return new RowColumn(row, column + 1);
    }

    public RowColumn bottomLeft() {
        return new RowColumn(row + 1, column - 1);
    }

    public RowColumn bottom() {
        return new RowColumn(row + 1, column);
    }

    public RowColumn bottomRight() {
        return new RowColumn(row + 1, column + 1);
    }
}

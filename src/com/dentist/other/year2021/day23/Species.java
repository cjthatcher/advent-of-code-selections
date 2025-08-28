package com.dentist.other.year2021.day23;

public enum Species {
    A(1, 2, "A"),
    B(10, 4, "B"),
    C(100, 6, "C"),
    D(1000, 8, "D"),
    NOPE(0, 0, ".");

    public final int cost;
    public final int targetColumn;
    public final String alias;

    private Species(int cost, int targetColumn, String name) {
        this.cost = cost;
        this.targetColumn = targetColumn;
        this.alias = name;
    }
}

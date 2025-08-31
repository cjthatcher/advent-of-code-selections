package com.dentist.other.year2021.day24;

public enum Symbol {
    W("w"),
    X("x"),
    Y("y"),
    Z("z"),
    EXTERNAL_INPUT("externalInput");

    public final String alias;

    private Symbol(String alias) {
        this.alias = alias;
    }

    public static Symbol find(String firstKey) {
        for (Symbol s : values()) {
            if (firstKey.equals(s.alias)) {
                return s;
            }
        }
        return null;
    }
}

package com.dentist.other.year2021.day24.chunking;

public abstract class Op {
    public static Op fromString(String part) {
        return switch (part) {
            case "add" -> new Add();
            case "mod" -> new Mod();
            case "mul" -> new Mul();
            case "div" -> new Div();
            case "eql" -> new Eql();
            default -> throw new IllegalArgumentException("Weird input for Operator");
        };
    }

    public abstract int execute(int a, int b);
}

class Mul extends Op {
    @Override
    public int execute(int a, int b) {
        return a * b;
    }
}

class Add extends Op {
    @Override
    public int execute(int a, int b) {
        return a + b;
    }
}

class Mod extends Op {
    @Override
    public int execute(int a, int b) {
        if (a < 0 || b <= 0) {
            throw new IllegalArgumentException("Can't mod a negative number, apparently.");
        }
        return a % b;
    }
}

class Div extends Op {
    @Override
    public int execute(int a, int b) {
        return a / b;
    }
}

class Eql extends Op {
    @Override
    public int execute(int a, int b) {
        return a == b ? 1 : 0;
    }
}
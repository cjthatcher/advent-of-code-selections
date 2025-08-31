package com.dentist.other.year2021.day24;


public abstract class Operation {

    public abstract int execute(int first, int second, ALU alu);

}

class Input extends BiFunction {

    private final String firstKey;

    public Input(String firstKey, int lineNumber) {
        super("inp", firstKey, "", 0, lineNumber);
        this.firstKey = firstKey;
    }

    @Override
    public int execute(int left, int right, ALU alu) {
        return alu.read(firstKey);
    }

}

abstract class BiFunction extends Operation {
    private String firstKey;
    private String secondKey;
    private int secondValue;

    public BiFunction(String alias, String firstKey, String secondKey, int secondValue, int lineNumber) {
        this.firstKey = firstKey;
        this.secondKey = secondKey;
        this.secondValue = secondValue;
    }

    public abstract int execute(int first, int second, ALU alu);
}

class Add extends BiFunction {

    public Add(String firstKey, String secondKey, int secondValue, int lineNumber) {
        super("+", firstKey, secondKey, secondValue, lineNumber);
    }

    @Override
    public int execute(int first, int second, ALU alu) {
        return first + second;
    }
}

class Mul extends BiFunction {

    public Mul(String firstKey, String secondKey, int secondValue, int lineNumber) {
        super("*", firstKey, secondKey, secondValue, lineNumber);
    }

    @Override
    public int execute(int first, int second, ALU alu) {
        return first * second;
    }
}

class Div extends BiFunction {

    public Div(String firstKey, String secondKey, int secondValue, int lineNumber) {
        super("/", firstKey, secondKey, secondValue, lineNumber);
    }


    @Override
    public int execute(int first, int second, ALU alu) {
        return first / second;
    }
}

class Mod extends BiFunction {

    public Mod(String firstKey, String secondKey, int secondValue, int lineNumber) {
        super("%", firstKey, secondKey, secondValue, lineNumber);
    }


    @Override
    public int execute(int first, int second, ALU alu) {
        return first % second;
    }
}

class Eql extends BiFunction {

    public Eql(String firstKey, String secondKey, int secondValue, int lineNumber) {
        super("==", firstKey, secondKey, secondValue, lineNumber);
    }

    @Override
    public int execute(int first, int second, ALU alu) {
        if (first == second) {
            return 1;
        } else
            return 0;
    }
}

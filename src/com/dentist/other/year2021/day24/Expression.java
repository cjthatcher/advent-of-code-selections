package com.dentist.other.year2021.day24;

import java.util.Optional;

public class Expression {

    // An expression can be a constant.
    // It can also be an expression operator expression.

    // it either represents:
    // 1. A symbol, meaning "read this and use its value"
    // 2. A constant, meaning the value is 25
    // 3. An input symbol, meaning read "w2" or whatever
    // 4. An expression op another expression.

    Operation operation;
    Expression left;
    Expression right;
    int constant;
    String symbol;

    public Expression(Optional<Integer> oInt, Optional<String> symbol, Optional<Expression> left, Optional<Expression> right, Optional<Operation> operation){
        constant = oInt.orElse(Integer.MIN_VALUE);
        this.symbol = symbol.orElse("");
        this.left = left.orElse(null);
        this.right = right.orElse(null);
        this.operation = operation.orElse(null);
    }

    @Override
    public String toString() {
        if (constant != Integer.MIN_VALUE) {
            return Integer.toString(constant);
        }

        if (!symbol.isEmpty()) {
            return symbol;
        }

//        if (left != null && right != null && operation != null) {
//            return String.format("(%s %s %s)", left.toString(), operation.alias(), right.toString());
//        }

        return "AH something went wrong";
    }

    public int simplify(ALU alu) {
        if (constant != Integer.MIN_VALUE) {
            return constant;
        }

        if (!symbol.isEmpty()) {
            return alu.read(symbol);
        }

        if (left != null && right != null && operation != null) {
            return operation.execute(left.simplify(alu), right.simplify(alu), alu);
        }

        return Integer.MIN_VALUE;
    }

    public void replace(String symbolToReplace, Expression expressionWithWhichToReplaceIt) {
        // If we are one symbol, then we need to become equivalent to our replacement expression.
        if (!symbol.isEmpty()) {
            symbol = "";
            this.left = expressionWithWhichToReplaceIt.left;
            this.right = expressionWithWhichToReplaceIt.right;
            this.operation = expressionWithWhichToReplaceIt.operation;
            this.constant = expressionWithWhichToReplaceIt.constant;
        }

        if (this.left != null && this.right != null) {
            this.left.replace(symbolToReplace, expressionWithWhichToReplaceIt);
            this.right.replace(symbolToReplace, expressionWithWhichToReplaceIt);
        }
    }

    public static Expression fromString(String s) {
        String[] parts = s.split(" ");

        String name = parts[0];
//        switch(name) {
//            case ""
//        }
        return null;
    }
}

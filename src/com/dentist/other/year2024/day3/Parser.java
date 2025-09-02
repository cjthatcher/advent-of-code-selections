package com.dentist.other.year2024.day3;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private final Pattern yes = Pattern.compile("do\\(\\)");
    private final Pattern not_yes = Pattern.compile("don't\\(\\)");
    private final Pattern mul = Pattern.compile("mul\\(\\d{1,3},\\d{1,3}\\)");

    boolean shouldMultiply = true;
    int total = 0;

    private String applyNextToken(String remaining) {
        Matcher doo = yes.matcher(remaining);
        doo.find();
        MatchResult a = doo.toMatchResult();

        Matcher doont = not_yes.matcher(remaining);
        doont.find();
        MatchResult b = doont.toMatchResult();

        Matcher mool = mul.matcher(remaining);
        mool.find();
        MatchResult c = mool.toMatchResult();

        int nextDo = a.hasMatch() ? a.start() : Integer.MAX_VALUE;
        int nextDoNot = b.hasMatch() ? b.start() : Integer.MAX_VALUE;
        int nextMul = c.hasMatch() ? c.start() : Integer.MAX_VALUE;

        if (nextDo < nextDoNot && nextDo < nextMul) {
            shouldMultiply = true;
            return remaining.substring(a.end());
        }

        if (nextDoNot < nextDo && nextDoNot < nextMul) {
            shouldMultiply = false;
            return remaining.substring(b.end());
        }

        if (nextMul < nextDo && nextMul < nextDoNot) {
            if (shouldMultiply) {
                total += multiply(remaining.substring(c.start(), c.end()));
            }
            return remaining.substring(c.end());
        }

        if (nextMul == nextDo && nextDo == nextDoNot && nextMul == Integer.MAX_VALUE) {
            return "";
        }

        throw new IllegalStateException("I should not have reached this point.");
    }

    public int consumeInput(String input) {
        String nextOne = input;
        while(!nextOne.isEmpty()) {
            nextOne = applyNextToken(nextOne);
        }
        return total;
    }

    /** Given a reasonable mul(x,y) clause, return the product of x and y. */
    public static int multiply(String clause) {
        String[] parts = clause.split(",");
        String y = parts[1].substring(0, parts[1].length() - 1); // lose the last character, which should be a ')';
        String x = parts[0].split("\\(")[1];
        return Integer.parseInt(x) * Integer.parseInt(y);
    }
}

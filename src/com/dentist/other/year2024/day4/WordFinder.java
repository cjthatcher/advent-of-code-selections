package com.dentist.other.year2024.day4;

import com.dentist.other.year2021.day23.Coordinate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordFinder {
    Map<Coordinate, Character> letters = new HashMap<>();
    private final char[] targetLetters;
    private final char middleChar;

    private int boundaryRow = 0;
    private int boundaryColumn = 0;

    public WordFinder(String targetWord, List<String> rows) {
        this.targetLetters = targetWord.toCharArray();
        this.middleChar = targetLetters[targetLetters.length / 2];
        int row = 0;
        boundaryRow = rows.size() - 1;
        boundaryColumn = rows.getFirst().length() - 1;
        for (String r : rows) {
            char[] chars = r.toCharArray();
            for (int column = 0; column < chars.length; column++) {
                letters.put(new Coordinate(row, column), chars[column]);
            }
            row++;
        }
    }

    public int count() {
        int count = 0;
        for (int row = 0; row <= boundaryRow; row++) {
            for (int column = 0; column <= boundaryColumn; column++) {
                count += exploreCell(new Coordinate(row, column));
            }
        }
        return count;
    }

    private int exploreCell(Coordinate cell) {
        int count = 0;
        if (letters.get(cell) == targetLetters[0]) {
            for (Coordinate d : Coordinate.directions) {
                count+= tryDirection(cell, d);
            }
        }
        return count;
    }

    public int starCount() {
        int count = 0;
        for (int row = 0; row <= boundaryRow; row++) {
            for (int column = 0; column <= boundaryColumn; column++) {
                count += exploreStar(new Coordinate(row, column));
            }
        }
        return count;
    }

    private int exploreStar(Coordinate cell) {
        // need the center cell to be the center letter,
        // Need UP_RIGHT from bottomLeft to be 1 || DOWN_LEFT from upRight
        // need bottomRight from topLeft

        if (letters.get(cell) == middleChar) {
            boolean rightSlash = tryDirection(cell.downLeft(), Coordinate.UP_RIGHT) + tryDirection(cell.upRight(), Coordinate.DOWN_LEFT) > 0;
            boolean leftSlash = tryDirection(cell.upLeft(), Coordinate.DOWN_RIGHT) + tryDirection(cell.downRight(), Coordinate.UP_LEFT) > 0;
            if (rightSlash && leftSlash) {
                return 1;
            }
        }

        return 0;
    }

    private int tryDirection(Coordinate start, Coordinate delta) {
        Coordinate current = start;
        for (int i = 0; i < targetLetters.length; i++) {
            if (letters.get(current) == null || letters.get(current) != targetLetters[i]) {
                return 0;
            }
            current = current.add(delta);
        }
        return 1;
    }

}

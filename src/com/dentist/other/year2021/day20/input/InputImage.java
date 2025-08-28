package com.dentist.other.year2021.day20.input;

import org.apache.kafka.common.protocol.types.Field;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class InputImage {

    Map<RowColumn, Boolean> inputValues;

    public int topRow = 0;
    public int leftColumn = 0;
    public int bottomRow = 0;
    public int rightColumn = 0;
    public final boolean defValue;

    public InputImage(Map<RowColumn, Boolean> inputValues, boolean defValue) {
        this.inputValues = inputValues;
        this.defValue = defValue;

        for (RowColumn rc : inputValues.keySet()) {
            if (rc.row() < topRow) {
                topRow = rc.row();
            }
            if (rc.row() > bottomRow) {
                bottomRow = rc.row();
            }
            if (rc.column() < leftColumn) {
                leftColumn = rc.column();
            }
            if (rc.column() > rightColumn) {
                rightColumn = rc.column();
            }
        }
    }

    public static InputImage fromString(String input, boolean defValue) {
        Map<RowColumn, Boolean> inputValues = new HashMap<>();

        String[] lines = input.split("\n");
        int row = 0;
        for (String line : lines) {
            int column = 0;
            for (char c : line.toCharArray()) {
                inputValues.put(new RowColumn(row, column++), c == '#');
            }
            row++;
        }

        return new InputImage(inputValues, defValue);
    }

    /** Given a row+column, figure out if the given square is bright (#) or if given square is dark (.) */
    public boolean val(RowColumn rc) {
        return inputValues.getOrDefault(rc, defValue);
    }

    /** Given a row+column, check out the nine adjacent squares, and figure out what int is represented. */
    public int getIntegerAround(int row, int column) {
        RowColumn a = new RowColumn(row, column);

        BitSet bs = new BitSet(9);
        bs.set(8, val(a.topLeft()));
        bs.set(7, val(a.top()));
        bs.set(6, val(a.topRight()));

        bs.set(5, val(a.left()));
        bs.set(4, val(a.center()));
        bs.set(3, val(a.right()));

        bs.set(2, val(a.bottomLeft()));
        bs.set(1, val(a.bottom()));
        bs.set(0, val(a.bottomRight()));

        if (bs.isEmpty()) {
            return 0;
        }

        return (int) bs.toLongArray()[0];
    }

    public int howManyLights() {
        return (int) inputValues.values().stream().filter(boo -> boo).count();
    }

    public void print() {
        for (int row = topRow; row <= bottomRow; row++) {
            StringBuilder sb = new StringBuilder();
            for (int column = leftColumn; column <= rightColumn; column++) {
                sb.append(val(new RowColumn(row, column)) ? '#' : '.');
            }
            System.out.println(sb.toString());
        }
    }
}

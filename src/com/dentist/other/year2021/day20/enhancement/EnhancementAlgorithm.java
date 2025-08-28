package com.dentist.other.year2021.day20.enhancement;

import com.dentist.other.year2021.day20.input.InputImage;
import com.dentist.other.year2021.day20.input.RowColumn;

import java.util.BitSet;

public class EnhancementAlgorithm {

    private final BitSet bitSet;

    private EnhancementAlgorithm(BitSet bitSet) {
        this.bitSet = bitSet;
    }

    public static EnhancementAlgorithm fromString(String input) {
        BitSet bs = new BitSet(512);
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            bs.set(i, chars[i] == '#');
        }

        return new EnhancementAlgorithm(bs);
    }

    public boolean isBright(int index) {
        return bitSet.get(index);
    }

    public InputImage enhance(InputImage original) {
        StringBuilder sb = new StringBuilder();
        for (int row = original.topRow - 1; row <= original.bottomRow + 1; row++) {
            for (int column = original.leftColumn - 1; column <= original.rightColumn + 1; column++) {

                int index = original.getIntegerAround(row, column);
                boolean bright = isBright(index);

//                if (index != 0) {
//                    System.out.println(String.format("At row %d, column %d, origValue %s, index %d we are %s", row, column, original.val(new RowColumn(row, column)) ? '#' : '.', index, bright ? "#" : "."));
//                }

                sb.append(bright ? '#' : '.');
            }
            sb.append("\n");
        }

        boolean defValue = false;
        if (!original.defValue) {
            defValue = isBright(0);
        } else {
            // what is 111111111?
            int allOnes = Integer.parseInt("111111111", 2);
            defValue = isBright(allOnes);
        }

        return InputImage.fromString(sb.toString(), defValue);
    }
}

package com.dentist.other.year2021.day23;

import java.awt.color.ICC_ColorSpace;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Driver {

    // Hallway is seven chars long, with a buffer of two on both sides.
    // Rooms are two deep.

    // Find the set of movable critters
    // Find the set of possible moves for each of those critters
    // DFS to see who gets to the winning configuration with the cheapest list of moves.

    public static void main(String[] args) throws FileNotFoundException {
        List<String> lines = new BufferedReader(new FileReader("resources/2021/23.txt")).lines().toList();

        Coordinate[] spots = new Coordinate[16];
        boolean[] used = new boolean[16];
        consumeLine(lines.get(2), 1, used, spots);
        consumeLine(lines.get(3), 2, used, spots);
        consumeLine(lines.get(4), 3, used, spots);
        consumeLine(lines.get(5), 4, used, spots);

        Scenario s = new Scenario(new ArrayList<>(),0, spots);
        System.out.println(s);

        //Gonna do some ding dang recursion here. DFS baby!

        Scenario winner = s.recurse(0, "");
        System.out.println(winner);
        System.out.println("Winner cost " + winner.totalCost);

        Scenario replay = s;
        for (Move m : winner.provenance) {
            System.out.println(m);
            replay = replay.applyMove(m);
            System.out.println(replay);
        }
    }

    private static void consumeLine(String line, int row, boolean[] used, Coordinate[] spots) {
        find('A', 0, line, row, used, spots);
        find('B', 4, line, row, used, spots);
        find('C', 8, line, row, used, spots);
        find('D', 12, line, row, used, spots);
    }

    private static void find(char target, int arrayOffset, String line, int row, boolean[] used, Coordinate[] spots) {
        char[] chars = line.toCharArray();

        int alreadyUsed = 0;
        for (int i = 0; i < 4; i++) {
            if (used[i + arrayOffset] == true) {
                alreadyUsed++;
            }
        }

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == target) {
                spots[arrayOffset + alreadyUsed] = new Coordinate(row, i - 1);
                used[arrayOffset + alreadyUsed] = true;
                alreadyUsed++;
            }
        }
    }
}

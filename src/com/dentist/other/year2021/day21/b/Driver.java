package com.dentist.other.year2021.day21.b;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class Driver {

    public static void main(String[] args) throws FileNotFoundException {
        List<String> lines = new BufferedReader(new FileReader("resources/2021/21.txt")).lines().toList();
        int p1Start = Integer.parseInt(lines.get(0).split("starting position: ")[1]);
        int p2Start = Integer.parseInt(lines.get(1).split("starting position: ")[1]);

        QuantumDirac d = new QuantumDirac();

        int aScore = 0;
        int bScore = 0;
        boolean aTurn = true;
        int aLocation = p1Start;
        int bLocation = p2Start;

        QuantumDirac.Wins result = d.play(1, 3, aTurn, aScore, bScore, aLocation, bLocation)
                .add(d.play(3, 4, aTurn, aScore, bScore, aLocation, bLocation))
                .add(d.play(6, 5, aTurn, aScore, bScore, aLocation, bLocation))
                .add(d.play(7, 6, aTurn, aScore, bScore, aLocation, bLocation))
                .add(d.play(6, 7, aTurn, aScore, bScore, aLocation, bLocation))
                .add(d.play(3, 8, aTurn, aScore, bScore, aLocation, bLocation))
                .add(d.play(1, 9, aTurn, aScore, bScore, aLocation, bLocation));

        System.out.printf("A won %d times, and B won %d times. The larger of those two numbers is %d.", result.aWins(), result.bWins(), (Math.max(result.aWins(), result.bWins())));
    }

}

package com.dentist.other.year2021.day21.b;

public class QuantumDirac {

    record Wins(long aWins, long bWins) {
        public Wins add(Wins right) {
           return new Wins(aWins + right.aWins, bWins + right.bWins);
        }
    }

    public Wins play(long multiplier, int howFarToMove, boolean aTurn, int aScore, int bScore, int aLocation, int bLocation) {
        // Listen, one universe will be a three.
        // 1, 3
        // 3, 4
        // 6, 5
        // 7, 6
        // 6, 7
        // 3, 8
        // 1, 9

        if (aTurn) {
            int nextLocation = ((aLocation + howFarToMove - 1) % 10) + 1;
            aScore += nextLocation;
            aLocation = nextLocation;
        } else {
            int nextLocation = ((bLocation + howFarToMove - 1) % 10) + 1;
            bScore += nextLocation;
            bLocation = nextLocation;
        }

        if (aScore >= 21) {
            return new Wins(multiplier, 0);
        } else if (bScore >= 21) {
            return new Wins(0, multiplier);
        }

        aTurn = !aTurn;

        return play(1 * multiplier, 3, aTurn, aScore, bScore, aLocation, bLocation)
                .add(play(3 * multiplier, 4, aTurn, aScore, bScore, aLocation, bLocation))
                .add(play(6 * multiplier, 5, aTurn, aScore, bScore, aLocation, bLocation))
                .add(play(7 * multiplier, 6, aTurn, aScore, bScore, aLocation, bLocation))
                .add(play(6 * multiplier, 7, aTurn, aScore, bScore, aLocation, bLocation))
                .add(play(3 * multiplier, 8, aTurn, aScore, bScore, aLocation, bLocation))
                .add(play(1 * multiplier, 9, aTurn, aScore, bScore, aLocation, bLocation));
        }
}

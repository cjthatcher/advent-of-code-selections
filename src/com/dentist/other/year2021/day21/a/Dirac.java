package com.dentist.other.year2021.day21.a;

public class Dirac {

    private boolean aTurn = true;
    private final Player a;
    private final Player b;
    private final DeterministicDie die;

    public Dirac(int aPosition, int bPosition, DeterministicDie die) {
        this.a = new Player(aPosition, "A");
        this.b = new Player(bPosition, "B");
        this.die = die;
    }

    public int play() {
        while (a.score < 1000 && b.score < 1000) {
            if (aTurn) {
                a.roll(die);
            } else {
                b.roll(die);
            }
            aTurn = !aTurn;
        }

        int finalScore = (a.score < b.score) ?  a.score * die.numberOfRolls : b.score * die.numberOfRolls;
        System.out.println("Game is over. A = " + a.score + ", B = " + b.score + ". Rolls = " + die.numberOfRolls + ", so final answer is " + finalScore);
        return finalScore;
    }
}

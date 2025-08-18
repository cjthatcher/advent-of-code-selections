package com.dentist.other.year2021.day21.a;

public class DeterministicDie {

    public DeterministicDie() {}

    int counter = 1;
    int numberOfRolls = 0;

    public int roll() {
        numberOfRolls++;
        if (counter == 101) {
            counter = 1;
        }
        return counter++;
    }

}

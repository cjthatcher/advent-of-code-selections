package com.dentist.other.year2021.day21.a;

public class Player {
    String name;
    int score = 0;
    int location;

    public Player(int startingLocation, String name) {
        this.location = startingLocation;
        this.name = name;
    }

    public void roll(DeterministicDie die) {
        int a = die.roll();
        int b = die.roll();
        int c = die.roll();
        int delta = a + b + c;
        int nextLocation = ((location + delta - 1) % 10) + 1;
        score += nextLocation;
        location = nextLocation;

        System.out.printf("Player %s rolls %d+%d+%d and moves to space %d for a total score of %d%n", name, a, b, c, nextLocation, score);
    }
}

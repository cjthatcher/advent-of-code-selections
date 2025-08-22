package com.dentist.other.year2021.day22.part2.Intersection;

public enum Liveliness {
    ALIVE, // We will try to populate this every time.
    WANTS_TO_BE_DEAD, // If all three ranges want to be dead, don't populate it.
    ALIVE_FRIENDS; // We will populate this only if it is paired with an Alive friend.
}

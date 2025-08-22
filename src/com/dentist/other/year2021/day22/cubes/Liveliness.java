package com.dentist.other.year2021.day22.cubes;

public enum Liveliness {
    ALIVE, // We will try to populate this every time, unless someone is dead or dead friends.
    WANTS_TO_BE_DEAD, // If there are three of these, don't put it.
    DEAD_FRIENDS, // We will try to populate this if the other two friends are dead.
    ALIVE_FRIENDS;
}

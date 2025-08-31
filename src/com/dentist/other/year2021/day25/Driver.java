package com.dentist.other.year2021.day25;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class Driver {

    public static void main(String[] args) throws FileNotFoundException {
        List<String> lines = new BufferedReader(new FileReader("resources/2021/25.txt")).lines().toList();
        SeaFloor firstGeneration = SeaFloor.fromInput(lines);
        System.out.println("Generation 0 = \r\n" + firstGeneration.toString());

        SeaFloor oldGeneration = firstGeneration;
        SeaFloor nextGeneration = firstGeneration.step();
        System.out.println("Generation 1 = \r\n" + nextGeneration.toString());
        int generations = 1;
        while (nextGeneration.isDifferent(oldGeneration)) {
            generations++;
            oldGeneration = nextGeneration;
            nextGeneration = oldGeneration.step();
            System.out.println("Generation " + generations + " =\r\n" + nextGeneration.toString());
        }

        System.out.println("FINAL ANSWER = " + generations);
    }
}

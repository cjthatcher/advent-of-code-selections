package com.dentist.other.year2024.day4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class Driver {

    public static void main(String[] args) throws FileNotFoundException {
        List<String> lines = new BufferedReader(new FileReader("resources/2024/4.txt")).lines().toList();
        int count = new WordFinder("XMAS", lines).count();
        System.out.println("The answer to part one is: " + count);

        int starCount = new WordFinder("MAS", lines).starCount();
        System.out.println("The answer to part two is: " + starCount);
    }
}

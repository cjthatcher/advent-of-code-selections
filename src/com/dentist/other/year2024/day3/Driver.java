package com.dentist.other.year2024.day3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

public class Driver {

    public static void main(String[] args) throws FileNotFoundException {
        List<String> lines = new BufferedReader(new FileReader("resources/2024/3.txt")).lines().toList();

        String combined = lines.stream().collect(Collectors.joining());
        int result = new Parser().consumeInput(combined);

        System.out.println("The final answer is: " + result);
    }

}

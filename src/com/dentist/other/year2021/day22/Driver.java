package com.dentist.other.year2021.day22;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class Driver {

    public static void main(String[] args) throws FileNotFoundException {
        List<String> lines = new BufferedReader(new FileReader("resources/2021/22-simple2.txt")).lines().toList();
        Core c = new Core();
        lines.stream().map(Instruction::fromString).forEach(c::applyInstruction);

        System.out.println("After consuming instructions, there are " + c.countOn() + " lights on.");
    }
}

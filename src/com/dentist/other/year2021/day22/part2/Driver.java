package com.dentist.other.year2021.day22.part2;

import com.dentist.other.year2021.day22.Instruction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class Driver {

    public static void main(String[] args) throws FileNotFoundException {
        List<String> lines = new BufferedReader(new FileReader("resources/2021/22.txt")).lines().toList();

        ComplexCore c = new ComplexCore();
        lines.stream().map(Instruction::fromString).forEach(c::applyInstruction);

        System.out.printf("There are %d lights on.%n", c.countLightsOn());
    }

    //2758514936282235

    // Not quite. DAMNIT. DAMN DAMN DAMN :)
    // I undercount by a little bit.
}

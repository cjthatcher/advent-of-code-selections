package com.dentist.other.year2021.day21.a;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class Driver {

    public static void main(String[] args) throws FileNotFoundException {
        List<String> lines = new BufferedReader(new FileReader("resources/2021/21.txt")).lines().toList();
        int p1Start = Integer.parseInt(lines.get(0).split("starting position: ")[1]);
        int p2Start = Integer.parseInt(lines.get(1).split("starting position: ")[1]);

        Dirac d = new Dirac(p1Start, p2Start, new DeterministicDie());
        d.play();
    }
}

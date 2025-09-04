package com.dentist.other.year2024.day5;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Driver {

    public static void main(String[] args) throws FileNotFoundException {
        List<String> lines = new BufferedReader(new FileReader("resources/2024/5.txt")).lines().toList();

        Set<Rule> rules = new HashSet<>();
        Set<PrintingPlan> plans = new HashSet<>();

        for (String s : lines) {
            if (s.contains("|")) {
                rules.add(Rule.fromString(s));
            } else if (s.contains(",")) {
                plans.add(PrintingPlan.fromString(s));
            }
        }

        part1(plans, rules);
        part2(plans, rules);
    }

    private static void part1(Set<PrintingPlan> plans, Set<Rule> rules) {
        int sum = 0;
        for (PrintingPlan p : plans) {
            if (p.satisfies(rules)) {
                sum += p.findMiddle();
            }
        }

        System.out.printf("Answer of part 1 is %d%n", sum);
    }

    private static void part2(Set<PrintingPlan> plans, Set<Rule> rules) {
        int sum = 0;
        for (PrintingPlan p : plans) {
            if (!p.satisfies(rules)) {
                PrintingPlan reformed = p.reorder(rules);
                sum += reformed.findMiddle();
            }
        }
        System.out.printf("Answer of part 2 is %d%n", sum);
    }
}

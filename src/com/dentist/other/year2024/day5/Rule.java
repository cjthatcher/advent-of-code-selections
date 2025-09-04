package com.dentist.other.year2024.day5;

import java.util.Collection;
import java.util.Map;

public class Rule {
    final String first;
    final String second;

    public Rule(String a, String b) {
        this.first = a;
        this.second = b;
    }

    public boolean satisfies(Map<String, Integer> printingPlan) {
        return !printingPlan.containsKey(first) ||
                !printingPlan.containsKey(second) ||
                printingPlan.get(first) < printingPlan.get(second);
    }

    public boolean applies(Collection<String> pages) {
        return pages.contains(first) && pages.contains(second);
    }

    public static Rule fromString(String input) {
        String[] parts = input.split("\\|");
        return new Rule(parts[0], parts[1]);
    }
}

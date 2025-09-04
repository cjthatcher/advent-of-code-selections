package com.dentist.other.year2024.day5;

import java.util.*;
import java.util.stream.Stream;

public class PrintingPlan {

    final Map<String, Integer> order;

    public static PrintingPlan fromString(String input) {
        Map<String, Integer> localOrder = new HashMap<>();

        String[] parts = input.split(",");
        for (int i = 0; i < parts.length; i++) {
            localOrder.put(parts[i], i);
        }
        return new PrintingPlan(localOrder);
    }

    private PrintingPlan(Map<String, Integer> localOrder) {
        this.order = localOrder;
    }

    public int findMiddle() {
        int midTarget = order.size() / 2;
        for (Map.Entry<String, Integer> e : order.entrySet()) {
            if (e.getValue() == midTarget) {
                return Integer.parseInt(e.getKey());
            }
        }
        throw new IllegalStateException("Should have had a middle page");
    }

    public boolean satisfies(Collection<Rule> rules) {
        for (Rule r : rules) {
            if (!r.satisfies(order)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Create a new printing plan with the same keys as this one, but with order fixed such that it satisfies the rules.
     */
    public PrintingPlan reorder(Set<Rule> rules) {
        // Who should go first?
        // I have a set of rules, a < b, e < f, c < k, etc.

        HashSet<String> pageNumbers = new HashSet<>(order.keySet());

        List<String> tempList = new LinkedList<>(order.keySet());
        List<Rule> applicableRules = rules.stream().filter(r -> r.applies(pageNumbers)).toList();

        // We may need to apply this up to n times to bubble sort the list properly. I think.
        for (int i = 0; i < tempList.size(); i++) {
            applicableRules.forEach(rule -> bubble(rule, tempList));
        }

        Map<String, Integer> resultingMap = new HashMap<>();
        int index =0;
        for (String s : tempList) {
            resultingMap.put(s, index++);
        }

        PrintingPlan resultingPlan = new PrintingPlan(resultingMap);
        if (resultingPlan.satisfies(rules)) {
            return resultingPlan;
        } else {
            throw new IllegalStateException("Could not reorder satisfactorily");
        }
    }

    private void bubble(Rule rule, List<String> tempList) {
        int a = tempList.indexOf(rule.first);
        int b = tempList.indexOf(rule.second);

        while (a > b) {
            System.out.printf("Hey, index of %s is %d, index of %s is %d%n", rule.first, a, rule.second, b);
            tempList.remove(a);
            tempList.add(a - 1, rule.first);
            a = tempList.indexOf(rule.first);
            b = tempList.indexOf(rule.second);
        }
    }
}

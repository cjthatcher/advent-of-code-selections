package com.dentist.other.year2021.day24;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModelNumberGenerator {

    private final List<Digit> digits;

    public ModelNumberGenerator() {
        digits = new ArrayList<Digit>();
        for (int i = 0; i < 14; i++) {
            Digit ahoy = new Digit();
            digits.add(ahoy);
            if (i > 0) {
                digits.get(i - 1).setNeighbor(ahoy);
            }
        }
    }

    public String nextModelNumber() {
        String num = digits.reversed().stream().map(i -> Integer.toString(i.value)).collect(Collectors.joining());
        digits.get(0).add();
        return num;
    }


}

class Digit {
    int value = 1;
    Digit neighbor;

    void add() {
        value++;
        if (value == 10) {
            value = 1;
            neighbor.add();
        }
    }

    void setNeighbor(Digit d) {
        this.neighbor = d;
    }
}

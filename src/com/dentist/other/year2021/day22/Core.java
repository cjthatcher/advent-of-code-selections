package com.dentist.other.year2021.day22;

import java.util.HashMap;
import java.util.Map;

public class Core {

    record Address(int x, int y, int z) {}
    private final Map<Address, Boolean> onsies = new HashMap<>();

    public void applyInstruction(Instruction i) {
        for (int x = i.xa(); x <= i.xb(); x++) {
            for (int y = i.ya(); y <= i.yb(); y++) {
                for (int z = i.za(); z <= i.zb(); z++) {
                    onsies.put(new Address(x, y, z), i.on());
                }
            }
        }
    }

    public long countOn() {
        return onsies.values().stream().filter(val -> val).count();
    }

}

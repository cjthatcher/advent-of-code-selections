package com.dentist.other.year2021.day24;

import java.util.*;

public class ALU {

    private final Map<String, Integer> memory = new HashMap<>();
    private final List<Integer> waitingInputs;
    private int cursor = 0;

    public ALU(String s) {
        waitingInputs = new ArrayList<Integer>(s.length());
        for (char c : s.toCharArray()) {
            waitingInputs.add(Integer.parseInt(String.valueOf(c)));
        }
    }

    public ALU(List<Integer> inputs) {
        this.waitingInputs = inputs;
    }

    private int get(String key) {
        return memory.getOrDefault(key, 0);
    }

    public int w() {
        return get("w");
    }

    public int x() {
        return get("x");
    }

    public int y() {
        return get("y");
    }

    public int z() {
        return get("z");
    }

    public void store(String key, int value) {
        memory.put(key, value);
    }

    public int nextInput() {
        if (cursor < waitingInputs.size()) {
            return waitingInputs.get(cursor++);
        } else {
            throw new RuntimeException("Asked for an input that didn't exist");
        }
    }

    public int read(String firstKey) {
        return get(firstKey);
    }

    @Override
    public String toString() {
        return String.format("w:%s, x:%s, y:%s, z:%s", w(), x(), y(), z());
    }
}

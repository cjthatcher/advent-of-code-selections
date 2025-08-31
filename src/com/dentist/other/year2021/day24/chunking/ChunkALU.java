package com.dentist.other.year2021.day24.chunking;

import java.util.HashMap;
import java.util.Map;

public class ChunkALU {

    private final Map<String, Integer> memory = new HashMap<>();
    private int cursor = 0;

    public ChunkALU(int w, int x, int y, int z) {
        memory.put("w", w);
        memory.put("x", x);
        memory.put("y", y);
        memory.put("z", z);
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

    public void write(String key, int value) {
        memory.put(key, value);
    }

    public int read(String firstKey) {
        return get(firstKey);
    }

    @Override
    public String toString() {
        return String.format("w:%s, x:%s, y:%s, z:%s", w(), x(), y(), z());
    }
}

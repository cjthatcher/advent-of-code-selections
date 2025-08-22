package com.dentist.other.year2021.day22.part2;

import com.dentist.other.year2021.day22.Instruction;
import com.dentist.other.year2021.day22.part2.cuboids.Cuboid;

import java.util.HashSet;
import java.util.Set;

public class ComplexCore {

    Set<Cuboid> cuboids = new HashSet<>();

    public void applyInstruction(Instruction i) {

        Cuboid interloper = new Cuboid(i.on, i.xa, i.xb, i.ya, i.yb, i.za, i.zb);

        Set<Cuboid> nextGeneration = new HashSet<Cuboid>();

        nextGeneration.add(interloper);
        for (Cuboid c : cuboids) {
            if (interloper.intersects(c)) {
                nextGeneration.addAll(c.fragmentAround(interloper));
            } else {
                nextGeneration.add(c);
            }
        }

        cuboids = nextGeneration;
    }

    public long countLightsOn() {
        long sum = 0;
        for (Cuboid c : cuboids) {
            if (c.isOn()) {
                sum += c.volume();
            }
        }
        return sum;
    }

    public void draw(String caption, int maxColumn, int maxRow, int maxDepth) {
        StringBuilder sb = new StringBuilder();
        sb.append(caption).append("\r\n");
        for (int depth = 0; depth < maxDepth; depth++) {
            sb.append("\r\n\r\n\r\n Depth = ").append(depth).append("\r\n");
            for (int row = maxRow; row >= 0; row--) {
                for (int column = 0; column < maxColumn; column++) {
                    Cuboid pixel = new Cuboid(true, column, column+1, row, row+1, depth, depth+1);
                    Light intersection = anyIntersects(pixel);
                    if (intersection == Light.ON) {
                        sb.append("*");
                    } else if( intersection == Light.OFF) {
                        sb.append("0");
                    } else {
                        sb.append(".");
                    }
                }
                sb.append("\r\n");
            }
        }
        System.out.print(sb);
    }

    enum Light {
        ON, OFF, NONE
    }

    private Light anyIntersects(Cuboid pixel) {
        for (Cuboid c : cuboids) {
            if (c.intersects(pixel)) {
                return c.isOn() ? Light.ON : Light.OFF;
            }
        }
        return Light.NONE;
    }
}

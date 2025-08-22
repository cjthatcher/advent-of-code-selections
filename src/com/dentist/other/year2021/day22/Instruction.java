package com.dentist.other.year2021.day22;

public class Instruction {

    public static final Instruction INVALID_INSTRUCTION = new Instruction(false, 0, -1, 0, -1, 0, -1); // will get skipped.

    public final int xa, xb, ya, yb, za, zb;
    public final boolean on;

    public Instruction(boolean on,
                       int xa, int xb, int ya, int yb, int za, int zb) {
        this.on = on;
        this.xa = xa;
        this.xb = xb + 1;
        this.ya = ya;
        this.yb = yb + 1;
        this.za = za;
        this.zb = zb + 1;

    }


    //on x=-20..26,y=-36..17,z=-47..7
    public static Instruction fromString(String s) {
        int xa = Integer.parseInt(s.split("x=")[1].split("\\.\\.")[0]);
        int xb = Integer.parseInt(s.split("\\.\\.")[1].split(",y=")[0]);

        int ya = Integer.parseInt(s.split("y=")[1].split("\\.\\.")[0]);
        int yb = Integer.parseInt(s.split("\\.\\.")[2].split(",z=")[0]);

        int za = Integer.parseInt(s.split("z=")[1].split("\\.\\.")[0]);
        int zb = Integer.parseInt(s.split("\\.\\.")[3]);

        boolean on = s.startsWith("on");

        return new Instruction(on, xa, xb, ya, yb, za, zb);
    }
}


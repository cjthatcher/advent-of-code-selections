package com.dentist.other.year2021.day22;

public record Instruction(boolean on,
                          int xa, int xb, int ya, int yb, int za, int zb) {

    public static final Instruction INVALID_INSTRUCTION = new Instruction(false, 0, -1, 0, -1, 0, -1); // will get skipped.

    //on x=-20..26,y=-36..17,z=-47..7
    public static Instruction fromString(String s) {
        int xa = Integer.parseInt(s.split("x=")[1].split("\\.\\.")[0]);
        int xb = Integer.parseInt(s.split("\\.\\.")[1].split(",y=")[0]);

        int ya = Integer.parseInt(s.split("y=")[1].split("\\.\\.")[0]);
        int yb = Integer.parseInt(s.split("\\.\\.")[2].split(",z=")[0]);

        int za = Integer.parseInt(s.split("z=")[1].split("\\.\\.")[0]);
        int zb = Integer.parseInt(s.split("\\.\\.")[3]);

        boolean on = s.startsWith("on");

//        if ((xa > 50 || ya > 50 || za > 50) || (xb < - 50 || yb < - 50 || zb < -50)) {
//            return INVALID_INSTRUCTION;
//        }
//
//        if (xa < -50) {
//            xa = -50;
//        }
//
//        if (xb > 50) {
//            xb = 50;
//        }
//
//        if (ya < -50) {
//            ya = -50;
//        }
//
//        if (yb > 50) {
//            yb = 50;
//        }
//
//        if (za < -50) {
//            za = -50;
//        }
//
//        if (zb > 50) {
//            zb = 50;
//        }

        return new Instruction(on, xa, xb, ya, yb, za, zb);
    }
}


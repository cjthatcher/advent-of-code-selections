package com.dentist.other.year2021.day19;

// Represents x, y, and z being positively or negatively flipped. It's just the 8 values of 3 bits. I'd represent it like bits if I were brave.
public enum Rescale {

    A(false, false, false),
    B(false, false, true),
    C(false, true, false),
    D(false, true, true),
    E(true, false, false),
    F(true, false, true),
    G(true, true, false),
    H(true, true, true);

    final boolean positiveX;
    final boolean positiveY;
    final boolean positiveZ;

    private Rescale(boolean positiveX, boolean positiveY, boolean positiveZ) {
        this.positiveX = positiveX;
        this.positiveY = positiveY;
        this.positiveZ = positiveZ;
    }

    public Coordinates3d apply(Coordinates3d vector) {
        return new Coordinates3d(vector.x * xFlip(), vector.y * yFlip(), vector.z * zFlip());
    }

    private int xFlip() {
        return positiveX ? 1 : -1;
    }

    private int yFlip() {
        return positiveY ? 1 : -1;
    }

    private int zFlip() {
        return positiveZ ? 1 : -1;
    }
}

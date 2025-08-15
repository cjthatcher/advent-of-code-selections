package com.dentist.other.year2021.day19;

/** This set of rotations is twice as big as necessary. Half of these are redundant. But that's okay for now. */
public enum Rotation {
    A(Reorder.A, Rescale.A),
    B(Reorder.A, Rescale.B),
    C(Reorder.A, Rescale.C),
    D(Reorder.A, Rescale.D),
    E(Reorder.A, Rescale.E),
    F(Reorder.A, Rescale.F),

    A1(Reorder.B, Rescale.A),
    B1(Reorder.B, Rescale.B),
    C1(Reorder.B, Rescale.C),
    D1(Reorder.B, Rescale.D),
    E1(Reorder.B, Rescale.E),
    F1(Reorder.B, Rescale.F),

    A2(Reorder.C, Rescale.A),
    B2(Reorder.C, Rescale.B),
    C2(Reorder.C, Rescale.C),
    D2(Reorder.C, Rescale.D),
    E2(Reorder.C, Rescale.E),
    F2(Reorder.C, Rescale.F),

    A3(Reorder.D, Rescale.A),
    B3(Reorder.D, Rescale.B),
    C3(Reorder.D, Rescale.C),
    D3(Reorder.D, Rescale.D),
    E3(Reorder.D, Rescale.E),
    F3(Reorder.D, Rescale.F),

    A4(Reorder.E, Rescale.A),
    B4(Reorder.E, Rescale.B),
    C4(Reorder.E, Rescale.C),
    D4(Reorder.E, Rescale.D),
    E4(Reorder.E, Rescale.E),
    F4(Reorder.E, Rescale.F),

    A5(Reorder.F, Rescale.A),
    B5(Reorder.F, Rescale.B),
    C5(Reorder.F, Rescale.C),
    D5(Reorder.F, Rescale.D),
    E5(Reorder.F, Rescale.E),
    F5(Reorder.F, Rescale.F),

    DO_NOT_ROTATE(Reorder.A, Rescale.A);

    final Reorder order;
    final Rescale scale;

    private Rotation(Reorder order, Rescale scale) {
        this.order = order;
        this.scale = scale;
    }

    public Coordinates3d apply(Coordinates3d vector) {
        return scale.apply(order.apply(vector));
    }
}

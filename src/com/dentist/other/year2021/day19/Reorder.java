package com.dentist.other.year2021.day19;

//Represents the order of coordinates. A represents an (x, y, z) coordinate system. B represents an (x, z, y) system, etc.
public enum Reorder {
    A(0, 1, 2),
    B(0, 2, 1),
    C(1, 0, 2),
    D(1, 2, 0),
    E(2, 1, 0),
    F(2, 0, 1);

    final int xOrder;
    final int yOrder;
    final int zOrder;

    private Reorder(int xOrder, int yOrder, int zOrder) {
        this.xOrder = xOrder;
        this.yOrder = yOrder;
        this.zOrder = zOrder;
    }

    public Coordinates3d apply(Coordinates3d vector) {
        int x = xOrder == 0 ? vector.x : yOrder == 0 ? vector.y : zOrder == 0 ? vector.z : 0;
        int y = xOrder == 1 ? vector.x : yOrder == 1 ? vector.y : zOrder == 1 ? vector.z : 0;
        int z = xOrder == 2 ? vector.x : yOrder == 2 ? vector.y : zOrder == 2 ? vector.z : 0;
        return new Coordinates3d(x, y, z);
    }
}

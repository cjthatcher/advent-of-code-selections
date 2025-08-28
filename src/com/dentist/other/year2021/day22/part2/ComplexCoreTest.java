package com.dentist.other.year2021.day22.part2;

import com.dentist.other.year2021.day22.Instruction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ComplexCoreTest {

    @Test
    public void instruction() {
        ComplexCore c = new ComplexCore();
        Instruction i = new Instruction(true, 0, 2, 0, 2, 0, 2);
        c.applyInstruction(i);
        Assertions.assertEquals(27, c.countLightsOn());

        Instruction other = new Instruction(true, 10, 12, 10, 12, 10, 12);

        c.applyInstruction(other);
        Assertions.assertEquals(54, c.countLightsOn());

        Instruction dark = new Instruction(false, 100, 102, 100, 102, 100, 102);
        c.applyInstruction(dark);
        Assertions.assertEquals(54, c.countLightsOn());

        // Pop one pixel in the center of the original cube
        Instruction darkCenter = new Instruction(false, 1, 1, 1, 1, 1, 1);
        c.applyInstruction(darkCenter);
        Assertions.assertEquals(53, c.countLightsOn());

        c.draw("This should show one inside pixel dark.", 5, 5, 5);

        // Completely eclipse second cuboid.
        Instruction eclipse = new Instruction(false, 9, 13, 9, 13, 9, 13);
        c.applyInstruction(eclipse);
        Assertions.assertEquals(26, c.countLightsOn());

        // Add a cuboid adjacent to the first one
        Instruction newFriend = new Instruction(true, 2, 4, 0, 2, 0, 2);
        c.applyInstruction(newFriend);
        //9 + 8 + 27 -> 44
        Assertions.assertEquals(44, c.countLightsOn());

        c.draw("This should show a 5 x 3 x 3 cube, with one pixel missing at depth = 1", 6, 6, 6);



        // Let's cut them both down the middle.
        Instruction bladeOfAiur = new Instruction(false, -1, 100, 1, 1, 2, 2);
        c.applyInstruction(bladeOfAiur);

        c.draw("This should show the previous cube (5 x 3 x 3) with a size 2 gash at depth = 2 and height = 0", 6, 6, 6);
        // this should remove 5.
        Assertions.assertEquals(39, c.countLightsOn());

        // Cut again, this time up and down
        Instruction blade2 = new Instruction(false, 1, 1, -100, 200, 2, 2);
        c.applyInstruction(blade2);

        c.draw("This should show the previous cube (5 x 3 x 3) with a size 1 gash at depth = 2 and x = 1", 6, 6, 6);
        // this should remove 2.
        Assertions.assertEquals(37, c.countLightsOn());
    }

    @Test
    public void inclusivity() {
        ComplexCore c = new ComplexCore();
        Instruction a = new Instruction(true, 10, 12, 10, 12, 10, 12);
        c.applyInstruction(a);

        Assertions.assertEquals(27, c.countLightsOn());
    }

    @Test
    public void overlap() {
        ComplexCore c = new ComplexCore();
        Instruction a = new Instruction(true, 0, 9, 0, 9, 0, 9);
        c.applyInstruction(a);

        Assertions.assertEquals(1000, c.countLightsOn());

        Instruction b = new Instruction(true, 1, 9, 1, 9, 1, 9);
        c.applyInstruction(b);
        Assertions.assertEquals(1000, c.countLightsOn());

        Instruction d = new Instruction(false, 2, 8, 2, 8, 2, 8);
        c.applyInstruction(d);

        Assertions.assertEquals(1000 - (7*7*7), c.countLightsOn());

        Instruction e = new Instruction(true, 3, 7, 3, 7, 3, 7);
        c.applyInstruction(e);

        Assertions.assertEquals(1000 - (7*7*7) + (5*5*5), c.countLightsOn());

        Instruction f = new Instruction(false, 4, 6, 4, 6, 4, 6);
        c.applyInstruction(f);

        Assertions.assertEquals(1000 - (7*7*7) + (5*5*5) - 27, c.countLightsOn());

        Instruction g = new Instruction(true, 0, 9, 0, 9, 0, 9);
        c.applyInstruction(g);

        Assertions.assertEquals(1000, c.countLightsOn());
    }
}

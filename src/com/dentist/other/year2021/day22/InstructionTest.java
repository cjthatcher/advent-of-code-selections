package com.dentist.other.year2021.day22;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InstructionTest {

    @Test
    public void parseOnTest() {
        String input = "on x=-20..26,y=-36..17,z=-47..7";
        Instruction result = Instruction.fromString(input);
        Instruction expected = new Instruction(true, -20, 26, -36, 17, -47, 7);

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void parseOffTest() {
        String input = "off x=-48..-32,y=26..41,z=-47..-37";
        Instruction result = Instruction.fromString(input);
        Instruction expected = new Instruction(false, -48, -32, 26, 41, -47, -37);

        Assertions.assertEquals(expected, result);
    }
}

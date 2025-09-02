package com.dentist.other.year2024.day3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParserTest {

    @Test
    public void testMulString() {
        String mul = "mul(3,100)";
        int result = Parser.multiply(mul);
        Assertions.assertEquals(300, result);
    }

    @Test
    public void testMulString2() {
        String mul = "mul(13,5)";
        int result = Parser.multiply(mul);
        Assertions.assertEquals(65, result);
    }

}

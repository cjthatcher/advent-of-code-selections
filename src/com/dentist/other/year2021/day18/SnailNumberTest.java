package com.dentist.other.year2021.day18;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SnailNumberTest {


    @Test
    public void explode1() {
        explosion("[[[[[9,8],1],2],3],4]", "[[[[0,9],2],3],4]");
    }

    @Test
    public void explode2() {
        explosion("[7,[6,[5,[4,[3,2]]]]]", "[7,[6,[5,[7,0]]]]");
    }

    @Test
    public void explode3() {
        explosion("[[6,[5,[4,[3,2]]]],1]", "[[6,[5,[7,0]]],3]");
    }

    @Test
    public void explode4() {
        explosion("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]", "[[3,[2,[8,0]]],[9,[5,[7,0]]]]");
    }

    private void explosion(String s, String s1) {
        SnailNumber test = SnailNumber.fromString(s);
        SnailNumber result = test.reduce();
        Assertions.assertEquals(s1, result.printMe());
    }

    @Test
    public void add_explode_split() {
        SnailNumber left = SnailNumber.fromString("[[[[4,3],4],4],[7,[[8,4],9]]]");
        SnailNumber right = SnailNumber.fromString("[1,1]");

        SnailNumber added = left.add(right);
        SnailNumber reduced = added.reduce();

        Assertions.assertEquals("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", reduced.printMe());
    }

    @Test
    public void magnitude1() {
        SnailNumber test = SnailNumber.fromString("[[1,2],[[3,4],5]]");
        Assertions.assertEquals(143, test.magnitude());
    }

    @Test
    public void magnitude2() {
        SnailNumber test = SnailNumber.fromString("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]");
        SnailNumber reduced = test.reduce();
        Assertions.assertEquals(3488, reduced.magnitude());
    }

    @Test
    public void listAdd() {
        String input = """
                [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
                [[[5,[2,8]],4],[5,[[9,9],0]]]
                [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
                [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
                [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
                [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
                [[[[5,4],[7,7]],8],[[8,3],8]]
                [[9,3],[[9,9],[6,[4,9]]]]
                [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
                [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]""";
        List<String> list = input.lines().toList();

        SnailNumber result = SnailNumber.addAll(list);

        Assertions.assertEquals("[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]", result.printMe());
        Assertions.assertEquals(4140, result.magnitude());
    }

    @Test
    public void failingAddition() {
        SnailNumber left = SnailNumber.fromString("[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]");
        SnailNumber right = SnailNumber.fromString("[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]");

        SnailNumber result = left.add(right);
        SnailNumber reduced = result.reduce();

        Assertions.assertEquals("[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]", reduced.printMe());
    }

    @Test
    public void listAdd2() {
        String input = """
                [[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
                [7,[[[3,7],[4,3]],[[6,3],[8,8]]]]
                [[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]
                [[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]
                [7,[5,[[3,8],[1,4]]]]
                [[2,[2,2]],[8,[8,1]]]
                [2,9]
                [1,[[[9,3],9],[[9,0],[0,7]]]]
                [[[5,[7,4]],7],1]
                [[[[4,2],2],6],[8,7]]""";
        List<String> list = input.lines().toList();

        SnailNumber result = SnailNumber.addAll(list);

        Assertions.assertEquals("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]", result.printMe());
    }

}

package com.dentist.other.year2021.day20.input;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class InputImageTest {

    @Test
    public void testThirtyFour() {
        Map<RowColumn, Boolean> inputValues = new HashMap<>();

        RowColumn middle = new RowColumn(1,1);

        //000-100-010 --> 34
        inputValues.put(middle.topLeft(), false);
        inputValues.put(middle.top(), false);
        inputValues.put(middle.topRight(), false);
        inputValues.put(middle.left(), true);
        inputValues.put(middle.center(), false);
        inputValues.put(middle.right(), false);
        inputValues.put(middle.bottomLeft(), false);
        inputValues.put(middle.bottom(), true);
        inputValues.put(middle.bottomRight(), false);

        InputImage inputImage = new InputImage(inputValues, false);
        int binaryNum = inputImage.getIntegerAround(1, 1);

        Assertions.assertEquals(34, binaryNum);
    }

    @Test
    public void test294() {
        Map<RowColumn, Boolean> inputValues = new HashMap<>();

        RowColumn middle = new RowColumn(1,1);

        //100-100-110 --> 294
        inputValues.put(middle.topLeft(), true);
        inputValues.put(middle.top(), false);
        inputValues.put(middle.topRight(), false);
        inputValues.put(middle.left(), true);
        inputValues.put(middle.center(), false);
        inputValues.put(middle.right(), false);
        inputValues.put(middle.bottomLeft(), true);
        inputValues.put(middle.bottom(), true);
        inputValues.put(middle.bottomRight(), false);

        InputImage inputImage = new InputImage(inputValues, false);
        int binaryNum = inputImage.getIntegerAround(1, 1);

        Assertions.assertEquals(294, binaryNum);
    }

    @Test
    public void test147() {
        Map<RowColumn, Boolean> inputValues = new HashMap<>();

        RowColumn middle = new RowColumn(1,1);

        //010-010-011 --> 294
        inputValues.put(middle.topLeft(), false);
        inputValues.put(middle.top(), true);
        inputValues.put(middle.topRight(), false);
        inputValues.put(middle.left(), false);
        inputValues.put(middle.center(), true);
        inputValues.put(middle.right(), false);
        inputValues.put(middle.bottomLeft(), false);
        inputValues.put(middle.bottom(), true);
        inputValues.put(middle.bottomRight(), true);

        InputImage inputImage = new InputImage(inputValues, false);
        int binaryNum = inputImage.getIntegerAround(1, 1);

        Assertions.assertEquals(147, binaryNum);
    }

    @Test
    public void test192(){
        Map<RowColumn, Boolean> inputValues = new HashMap<>();

        RowColumn middle = new RowColumn(1,1);

        //011-000-000 --> 294
        inputValues.put(middle.topLeft(), false);
        inputValues.put(middle.top(), true);
        inputValues.put(middle.topRight(), true);
        inputValues.put(middle.left(), false);
        inputValues.put(middle.center(), false);
        inputValues.put(middle.right(), false);
        inputValues.put(middle.bottomLeft(), false);
        inputValues.put(middle.bottom(), false);
        inputValues.put(middle.bottomRight(), false);

        InputImage inputImage = new InputImage(inputValues, false);
        int binaryNum = inputImage.getIntegerAround(1, 1);

        Assertions.assertEquals(192, binaryNum);
    }
}

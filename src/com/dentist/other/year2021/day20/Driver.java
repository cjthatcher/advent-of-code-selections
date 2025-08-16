package com.dentist.other.year2021.day20;

import com.dentist.other.year2021.day20.enhancement.EnhancementAlgorithm;
import com.dentist.other.year2021.day20.input.InputImage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

public class Driver {

    public static void main(String[] args) throws FileNotFoundException {
        List<String> lines = new BufferedReader(new FileReader("resources/2021/20.txt")).lines().toList();

        // the first line is the EnhancementAlgorithm
        EnhancementAlgorithm ea = EnhancementAlgorithm.fromString(lines.get(0));

        List<String> restOfLines = lines.subList(2, lines.size());
        String bigString = String.join("\n", restOfLines);

        // Note that the default on the first image is dark
        InputImage input = InputImage.fromString(bigString, false);

        System.out.println("The original image has " + input.howManyLights() + " lights.");
        System.out.println("Here is the original image: ");
        input.print();
        InputImage firstPass = ea.enhance(input);
        System.out.println("After first pass, there are " + firstPass.howManyLights() + " lights.");
        System.out.println("Here is the first pass image: ");
        firstPass.print();
        InputImage secondPass = ea.enhance(firstPass);
        System.out.println("After second pass, there are " + secondPass.howManyLights() + " lights.");
        System.out.println("Here is the second pass image: ");
        secondPass.print();

        InputImage result = secondPass;
        for (int i = 0; i < 48; i++) {
            result = ea.enhance(result);
        }

        System.out.println("After 50 total passes, there are " + result.howManyLights() + " lights.");
        System.out.println("Here is the 50th pass image: ");
        result.print();
    }
}

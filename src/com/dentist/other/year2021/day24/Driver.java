package com.dentist.other.year2021.day24;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Driver {

    public static void main(String[] args) throws FileNotFoundException {
        List<String> lines = new BufferedReader(new FileReader("resources/2021/24.txt")).lines().toList();
        List<Operation> operations = new ArrayList<>();

        int lineNumber = 0;
//        for (String s : lines) {
//            operations.add(Operation.fromString(s, lineNumber++));
//        }

        Operation last = operations.getLast();



        // split the operations by inp

//        List<List<Operation>> separatedByInp = new ArrayList<>();
//        List<Operation> temp = new ArrayList<>();
//        for (Operation o : operations) {
//            if (o instanceof Input) {
//                if (!temp.isEmpty()) {
//                    separatedByInp.add(temp);
//                    temp = new ArrayList<>();
//                }
//            }
//
//            temp.add(o);
//        }
//        separatedByInp.add(temp);
//
//        System.out.println("separated by inp has size " + separatedByInp.size());
//
//
//        for (int i = 1; i < 10; i++) {
//            ALU a = new ALU(Collections.singletonList(i));
//            for (Operation o : separatedByInp.get(0)) {
//                o.execute(a);
//            }
//
//            System.out.println("For input " + i + ", the first segment produces: " );
//            System.out.println(a);
//            System.out.println();
//        }

//        ModelNumberGenerator generator = new ModelNumberGenerator();
//        String inputString = generator.nextModelNumber();
//        while (!inputString.equals("99999999999999")) {
//            ALU alu = new ALU(inputString);
//            operations.forEach(o -> o.execute(alu));
////            System.out.printf("After executing %s, z is %d%n", inputString, alu.z());
//            if (alu.z() == 0) {
//                System.out.println(String.format("%s was zero", inputString));
//            }
//            inputString = generator.nextModelNumber();
//        }


    }
}

package com.dentist.other.year2021.day24.chunking;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Driver {

    public static void main(String[] args) throws FileNotFoundException, ExecutionException, InterruptedException {
        // What is the range of Z values we can get from the first six blocks?

        List<List<String>> inputs = new ArrayList<List<String>>();

        for (int i = 1; i < 15; i++) {
            List<String> lines = new BufferedReader(new FileReader(String.format("resources/2021/24/chunky%d.txt", i))).lines().toList();
            inputs.add(lines);
        }

        Map<Integer, SortedSet<InputWResultingZeta>> hackyZPossibilitiesMap = new ConcurrentHashMap<Integer, SortedSet<InputWResultingZeta>>();
        for (int i = 0; i < 15; i++) {
            hackyZPossibilitiesMap.put(i, Collections.synchronizedSortedSet(new TreeSet<>()));
        }

        hackyZPossibilitiesMap.get(14).add(new InputWResultingZeta(0,0, 0));

        // Ugh saying 1 based chunks.

        // Based on previous runs, I know reasonable ranges of Zs we should try. We use these here to save some runtime.
        Range[] rangeOfPreviousZToExpect = new Range[]{
                new Range(0,0),
                new Range(0,20),
                new Range(0,530),
                new Range(0,13798),
                new Range(0,530),
                new Range(0,13799),
                new Range(0,358795),
                new Range(0,9328684),
                new Range(12,358795),
                new Range(312,9328686),
                new Range(312,358795),
                new Range(312,13799),
                new Range(314,530),
                new Range(12,20)};

        // We know that if we can produce z = the value in the hackyZPossibilitiesMap, that we're okay.
        // So, what values of Z and what input will give us what we need?

        ExecutorService executor = Executors.newFixedThreadPool(36);

        for (int chunk = 14; chunk > 0; chunk--) {
            TreeSet<Integer> validZTargets = hackyZPossibilitiesMap.get(chunk).stream().map(tuple -> tuple.previousZ).collect(Collectors.toCollection(TreeSet::new));
            Range chunkRange = rangeOfPreviousZToExpect[chunk - 1];

            Set<CompletableFuture<Void>> futureWs = new HashSet<>();
            for (int wInput = 1; wInput < 10; wInput++) {
                // This would be an easy place to parallelize.
                // with parallelizing on the W's, (nine threads runnning per chunk) we end up at 45 seconds.
                // But what if we further parallelize by chunk? There are about 10million values in the biggest chunk.
                // Split it to one mil a piece.

                // Ends up that further splitting the ranges doesn't give us any real savings.
                for (Range miniRange : chunkRange.splitTo(100_000_000)) {
                    RunOneW parallelRun = new RunOneW(miniRange, wInput, inputs, chunk, hackyZPossibilitiesMap, validZTargets);
                    CompletableFuture<Void> f = CompletableFuture.runAsync(parallelRun, executor);
                    futureWs.add(f);
                }
            }

            // wait for all ten to complete.
            // We used to take 3 min 40 seconds to run this. Parallelized takes about 42 seconds.
            CompletableFuture.allOf(futureWs.toArray(new CompletableFuture[]{})).get();

            System.out.println("At the end of chunk " + chunk + ", we got " + hackyZPossibilitiesMap.get(chunk - 1).size() + " combos of z, inputs that could work!");
            TreeSet<Integer> zValues = hackyZPossibilitiesMap.get(chunk - 1).stream().map(input -> input.previousZ).collect(Collectors.toCollection(TreeSet::new));
            System.out.println("min = " + zValues.first() + " Max = " + zValues.last());
        }

        System.out.println("HAHOAHOHDSAFOHSFDGKJHSFGKLJHFDGKJHGF we made it to the middle?!?");




        // Okay, so prepare yourself for some hackiness!

        int zWeNeedInNextChunk = 0;
        List<Integer> bestInputs = new ArrayList<>();
        for (int chunk = 0; chunk < 14; chunk++) {

            boolean satisfied = false;
            while(!satisfied) {
                InputWResultingZeta bestPickFromThisChunk = hackyZPossibilitiesMap.get(chunk).removeLast();
                if (bestPickFromThisChunk.previousZ == zWeNeedInNextChunk) {
                    satisfied = true;
                    zWeNeedInNextChunk = bestPickFromThisChunk.resultingZ;
                    bestInputs.add(bestPickFromThisChunk.inputW);
                }
            }
            System.out.println("At the end of chunk " + chunk + ", we picked w= " + bestInputs.get(chunk) + " for next z=" + zWeNeedInNextChunk);
        }

        System.out.println("Here is the final result.");
        System.out.println(bestInputs.stream().map(i -> Integer.toString(i)).collect(Collectors.joining()));
        executor.shutdown();

        // For the final chunk, if z = w + 11, we are chicken dinners.
        // So there are 9 valid configurations for the final chunk.
        // The output of chunk 13 needs to give me a Z between 12 and 20

        // The transform done in chunk 14 is --> Z = Z - 11 - W.

        // My theory is that the transform done in chunk 13 is --> Z = Z -1 - W.
        // Nope, way weirder than that. Let's go through it..


        // K, so for chunk 13 (the penultimate), we need z = input + 1 + (26 * [12-20]) // anything else and it flies off the rails.
        // err, we know the final model number can end in 99. So that's neato burrito.
        // I could do this by hand a few more times. And once I've done that I guess I can brute force the rest.
        // Is there a way I can automate this?
        // Given a range of z output values, can I find a range of w and z input values that get the job done?
        // I could brute force it. I _should_ be able to reason about it from the instructions.

        // For chunk 12, I need Z to come out as (1 + (26 * [12,20]) + [0,9]). So there are 10 possible values I want.

        // 1 + (26 * 12) + 1   --> 314?
        // 1 + (26 * 13) + 2   --> 341
        // ...
        // 1 + (26 * 20) + 9   --> 530


        //        13 winner ---> ChunkALU chunkyAlu = new ChunkALU(input, 0, 0,  input + 1 + (26* 20));

//        for (int i = 1; i < 10; i++) {
//            for (int z = 0; z < 100_000; z++) {
//                ChunkALU chunkyAlu = new ChunkALU(i, 0, 0,  z);
//                for (String s : lines) {
//                    String[] parts = s.split(" ");
//                    if (parts.length > 2) {
//                        String targetSymbol = parts[1];
//                        Op operator = Op.fromString(parts[0]);
//                        String rightHand = parts[2];
//                        int right = 0;
//
//                        if (Character.isAlphabetic(rightHand.charAt(0))) {
//                            right = chunkyAlu.read(rightHand);
//                        } else {
//                            right = Integer.parseInt(parts[2]);
//                        }
//
//                        chunkyAlu.write(targetSymbol, operator.execute(chunkyAlu.read(targetSymbol), right));
//                    }
//                }
//                if (acceptableOutputs.contains(chunkyAlu.read("z"))) {
//                    System.out.println("For chunk 12, found an acceptable pair at input = " + i + " and z = " + z + ". output value is " + chunkyAlu.read("z"));
//                }
//            }
//
//        }


        // A winning strategy here should be input = 1, z = 26 + 11 + input.

        // So what if Z = - 26 + 11 + input? That's z = -14, input = 1?

        //inp w
        //mul x 0  --> Oh, x is zero now.
        //add x z  --> x == x + z. So, x = z. So Z needs to equal some multiple of 26?
        //mod x 26 --> x is now z % 26. Which we want to be zero. So z needs to be 26 + 11 + input here. Or something like that?
        //div z 26  --> z is now some smaller integer.
        //add x -11 --> x - 11; So x needs to be input + 11?
        //eql x w   --> if x == input, then x = 1
        //eql x 0   --> if x was NOT == input, then x now = 1. Otherwise now = 0. So, we know a winning strategy here is set x = input?
        // YES, can confirm, if x == input at this block, we have a valid model number, it appears.

        //mul y 0   --> y set to zero
        //add y 25  --> y = 25
        //mul y x   --> y is now either 25 or 0.
        //add y 1   --> y is now 26 or 1.  So, in order to get z to zero, we need z to be zero BEFORE this step?
        //mul z y   --> z will now be 26z or z. We need z to be between -3 and -11. So, going to have to have y be 1. X needs to be zero, and z needs to be [-3,-11]

        //mul y 0   --> y = 0 Set to zero. Ignore previous y.
        //add y w   --> y + input
        //add y 2   --> y + 2 (between 3 and 11 inclusive)
        //mul y x   --> (x is either 0 or 1 here. So y is either y or zero. Could z be negative somehow?)
        //add z y   --> y = -z


        // Here is chunk 13


        // input, w = 1, z = 10

        // We want the final z to be between 12 and 20.  So if z comes in here at, say, 26... So Z needs to be 26 * 10 through 26 * 20. As long as z %26 != input.

        //mul x 0
        //add x z   --> x = 26
        //mod x 26  ---> x = 0
        //div z 26  --> z = 1
        //add x -1  --> x = -1
        //eql x w   --> (x == w? Since x == z - 1, does w want to be z - 1?) (Well, we don't want a zero here now?)
        //eql x 0   --> X is probably 1 here.
        //mul y 0
        //add y 25  --> y = 25
        //mul y x   --> y = 25
        //add y 1   --> y = 26
        //mul z y   --> z = z == 26 now.... (original z wants to be > 26)
        //mul y 0   --> y == 0
        //add y w   --> y = input
        //add y 2   --> y = input + 2
        //mul y x   --> y = input + 2
        //add z y   --> z = z + input + 2.
    }

    public static class RunOneW implements Runnable {

        private final Range chunkRange;
        private final int wInput;
        private final List<List<String>> inputs;
        private final int chunk;
        private final Map<Integer, SortedSet<InputWResultingZeta>> hackyZPossibilitiesMap;
        private final TreeSet<Integer> validZTargets;

        RunOneW(Range chunkRange, int wInput, List<List<String>> inputs, int chunk, Map<Integer, SortedSet<InputWResultingZeta>> hackyZPossibilitiesMap, TreeSet<Integer> validZTargets) {
            this.chunkRange = chunkRange;
            this.wInput = wInput;
            this.inputs = inputs;
            this.chunk = chunk;
            this.hackyZPossibilitiesMap = hackyZPossibilitiesMap;
            this.validZTargets = validZTargets;
        }


        @Override
        public void run() {
            for (int potentialZFromEarlierChunk = chunkRange.start(); potentialZFromEarlierChunk <= chunkRange.end(); potentialZFromEarlierChunk++) {
                ChunkALU chunkyAlu = new ChunkALU(wInput, 0, 0, potentialZFromEarlierChunk);
                for (String s : inputs.get(chunk - 1)) {
                    executeLine(s, chunkyAlu);
                }

                // If this chunk got "potentialZ" from the block before it, and wInput was winput, then we'll end up with z = zeta here.
                int zeta = chunkyAlu.read("z");

                if (validZTargets.contains(zeta)) {
                    // If you pick a "2", and the old value in z was ",potentialZ", then you will get a valid zeta.
                    // Will need to pick a key that has the z value we produced from the previous chunk's biggest input.
                    // Then pick the key with a matching z value that has the greatest w input.
                    hackyZPossibilitiesMap.get(chunk - 1).add(new InputWResultingZeta(wInput, potentialZFromEarlierChunk, zeta));
                }
            }
        }
    }


    private static void executeLine(String s, ChunkALU chunkyAlu) {
        String[] parts = s.split(" ");
        if (parts.length > 2) {
            String targetSymbol = parts[1];
            Op operator = Op.fromString(parts[0]);
            String rightHand = parts[2];
            int right = 0;

            if (Character.isAlphabetic(rightHand.charAt(0))) {
                right = chunkyAlu.read(rightHand);
            } else {
                right = Integer.parseInt(parts[2]);
            }

            chunkyAlu.write(targetSymbol, operator.execute(chunkyAlu.read(targetSymbol), right));
        }
    }

    record InputWResultingZeta(int inputW, int previousZ, int resultingZ) implements Comparable<InputWResultingZeta> {
        public static InputWResultingZeta fromString(String s, Integer value) {
            String[] parts = s.split(",");
            return new InputWResultingZeta(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), value);
        }

        @Override
        public int compareTo(InputWResultingZeta o) {
            if (inputW != o.inputW) {
                return inputW - o.inputW;
            }

            if (previousZ != o.previousZ) {
                return previousZ - o.previousZ;
            }

            if (resultingZ != o.resultingZ) {
                return resultingZ - o.resultingZ;
            }

            return 0;
        }
    }
}

record Range(int start, int end) {

    public Range[] splitTo(int size) {
        int numberOfParts = ((end-start) / size + 1);
        Range[] parts = new Range[numberOfParts];

        for (int i = 0; i < numberOfParts; i++) {
            int whereToStart = start + (size * i);
            parts[i] = new Range(whereToStart, Math.min(whereToStart + size, end));
        }
        return parts;
    }
};
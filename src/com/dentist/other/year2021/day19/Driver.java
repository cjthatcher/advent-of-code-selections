package com.dentist.other.year2021.day19;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Stream;

public class Driver {

    //--- scanner 0 ---
    //-594,397,693
    //
    //--- scanner 1 ---
    public static void main(String[] args) throws FileNotFoundException {
        Stream<String> lines = new BufferedReader(new FileReader("resources/2021/19.txt")).lines();

        int index = 0;
        String currentParent = "foo";
        Scanner currentScanner = null;

        Set<Scanner> scanners = new HashSet<>();

        for (String line : lines.toList()) {
            if (line.startsWith("--- scanner")) {
                if (currentScanner != null) {
                    scanners.add(currentScanner);
                }
                index = 0;
                String name = line.split(" ")[2];
                currentParent = name;
                currentScanner = new Scanner(0,0,0, name);
            } else {
                if (!line.isEmpty()) {
                    Beacon b = Beacon.fromString(line, currentParent, index++);
                    currentScanner.addBeacon(b);
                }
            }
        }
        scanners.add(currentScanner);

        Scanner combined = Scanner.combineScanners(new ArrayList<Scanner>(scanners.stream().toList()));
        System.out.println("There are " + combined.knownBeacons.size() + " beacons");

        List<Map.Entry<String, Coordinates3d>> immutableAnnoyingList = combined.childScanners.entrySet().stream().toList();

        List<Map.Entry<String, Coordinates3d>> scannerLocations = new ArrayList<>(immutableAnnoyingList);
        scannerLocations.add(Map.entry("home", new Coordinates3d(0,0,0)));

        long longestManhattanDistance = 0;
        for (Map.Entry<String, Coordinates3d> a : scannerLocations) {
            for (Map.Entry<String, Coordinates3d> b : scannerLocations) {
                long currentManhattan = Coordinates3d.manhattanDistance(a.getValue(), b.getValue());
                System.out.println(a.getKey() + ":" + a.getValue() + " to " + b.getKey() + ":" + b.getValue() + "===" + currentManhattan);
                if (currentManhattan > longestManhattanDistance) {
                    longestManhattanDistance = currentManhattan;
                }
            }
        }
        System.out.println("Longest manhattan = " + longestManhattanDistance);
    }
}

package com.dentist.other.year2021.day19;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class Scanner {

    Set<Beacon> knownBeacons = new HashSet<Beacon>();
    int xOrigin, yOrigin, zOrigin;
    String name;
    Map<String, Coordinates3d> childScanners = new HashMap<>();

    public Coordinates3d origin() {
        return new Coordinates3d(xOrigin, yOrigin, zOrigin);
    }

    public static Scanner fromString(String input, String name) {
        Scanner s = new Scanner(0, 0, 0, name);
        List<String> lines = input.lines().toList();
        int i = 0;
        for (String line : lines) {
            s.addBeacon(Beacon.fromString(line, name, i++));
        }
        return s;
    }

    public Scanner(int xO, int yO, int zO, String name) {
        this.name = name;
        this.xOrigin = xO;
        this.yOrigin = yO;
        this.zOrigin = zO;
    }

    public void addBeacon(Beacon b) {
        knownBeacons.add(b);
    }

    public record Transformation(Rotation r, Coordinates3d rehomeVector) { }

    public Transformation findTransform(Scanner otherScanner) {
        Overlap o = findOverlap(otherScanner);
        if (o.sameBeacons().size() >= 12) {
            Coordinates3d a = o.representativeSamples().representativeAVector();
            Coordinates3d b = o.representativeSamples().representativeBVector();

            Rotation winningRotation = Rotation.DO_NOT_ROTATE;

            for (Rotation r : Rotation.values()) {
                if (r.apply(b).equals(a)) {
                    winningRotation = r;
                    break;
                }
            }

            if (winningRotation == Rotation.DO_NOT_ROTATE) {
                // failed to find a suitable rotation. Die.
                return null;
            }

            var aCoords = o.representativeSamples().a().coordinates;
            var rotatedCcoords = winningRotation.apply(o.representativeSamples().c().coordinates);
            var rehomeVector = aCoords.delta(rotatedCcoords);

            return new Transformation(winningRotation, rehomeVector);
        }
        return null;
    }

    public Scanner applyTransformation(Transformation transform) {
        // We need to rotate every one of our beacons, and add the rehome vector. Then our origin will be relative to A, and beacons will also be relative to A.
        Set<Beacon> b = knownBeacons.stream().map(oldBeacon -> oldBeacon.transform(transform)).collect(toSet());
        var s = new Scanner(transform.rehomeVector.x, transform.rehomeVector.y, transform.rehomeVector.z, name);

        childScanners.replaceAll((k, v) -> transform.r.apply(v).add(transform.rehomeVector));
        b.forEach(s::addBeacon);
        s.childScanners = childScanners;
        return s;
    }

    public void merge(Scanner other) {
        // eat all of their beacons. If we already have a beacon at that coordinate do not eat it.

        childScanners.put(other.name, other.origin());

        for (Map.Entry<String, Coordinates3d> otherChildScanner : other.childScanners.entrySet()) {
            if (!childScanners.containsKey(otherChildScanner.getKey())) {
                childScanners.put(otherChildScanner.getKey(), otherChildScanner.getValue());
            }
        }

        HashSet<Coordinates3d> existingCoordinates = new HashSet<>();
        for (Beacon b : knownBeacons) {
            existingCoordinates.add(b.coordinates);
        }

        for (Beacon b : other.knownBeacons) {
            if (!existingCoordinates.contains(b.coordinates)) {
                addBeacon(b);
                existingCoordinates.add(b.coordinates);
            }
        }


    }

    public Overlap findOverlap(Scanner otherScanner) {
        // To find the overlapping beacons -->

        // 1. Create a 2d array of beacon distances. The distance from each beacon to each of its known beacons.
        // 2. Create that same 2d array for the other scanner. In the test data, that's neighborhood of 26 * 26. Not too bad.

        // We will see that a beacon in scanner A has at least 12 distances similar to a beacon in Scanner B.
        // We know that's an overlapping beacon.
        // We also know that the 11 distances it has in common belong to shared folks.

        Distances aDistances = createDistancesByBeacon();
        Distances bDistances = otherScanner.createDistancesByBeacon();
        return aDistances.findOverlap(bDistances);
    }

    private Distances createDistancesByBeacon() {
        Map<Beacon, Map<Beacon, Double>> distancesByBeacon = new HashMap<>();
        for (Beacon b : knownBeacons) {
            distancesByBeacon.put(b, computeDistanceToNeighbors(b));
        }
        return new Distances(distancesByBeacon, name);
    }

    private Map<Beacon, Double> computeDistanceToNeighbors(Beacon b) {
        Map<Beacon, Double> distances = new HashMap<>();
        for (Beacon otherBeacon : knownBeacons) {
            if (b.equals(otherBeacon)) {
                continue;
            }
            double distance = b.coordinates.distance(otherBeacon.coordinates);
            distances.put(otherBeacon, distance);
        }
        return distances;
    }

    public static Scanner combineScanners(List<Scanner> scanners) {
        // pairwise, try to find some scanners that match.

        Scanner consumedScanner = null;
        if (scanners.size() == 1) {
            return scanners.stream().findFirst().get();
        }

        Collections.shuffle(scanners);
        for (Scanner a : scanners) {
            if (consumedScanner != null) {
                break;
            }

            for (Scanner b : scanners) {
                if (a == b) {
                    continue;
                }

                Overlap o = a.findOverlap(b);
                if (o.sameBeacons().size() >= 12) {
                    // if this overlaps, I need to consume b into a, and remove b from the set.
                    Transformation t = a.findTransform(b);
                    if (t == null || t.r == Rotation.DO_NOT_ROTATE) {
                        continue;
                    }

                    System.out.println("Merging " + b.name + " into " + a.name);
                    a.merge(b.applyTransformation(t));
                    consumedScanner = b;
                    break;

                }
            }
        }

        if (consumedScanner != null && scanners.size() > 2) {
            List<Scanner> nextScanners = new ArrayList<Scanner>();
            for (Scanner s : scanners) {
                if (!s.equals(consumedScanner)) {
                    nextScanners.add(s);
                }
            }
            return combineScanners(nextScanners);
        } else if (consumedScanner != null && scanners.size() == 2) {
            // we did it, we combined them down into one.
            for (Scanner s : scanners) {
                if (!s.equals(consumedScanner)) {
                    return s;
                }
            }
        }

        throw new RuntimeException("Failed to merge scanners. Something's in the way");
    }
}


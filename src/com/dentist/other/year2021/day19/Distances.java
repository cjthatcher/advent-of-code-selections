package com.dentist.other.year2021.day19;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Distances {
    Map<Beacon, Map<Beacon, Double>> distancesByBeacon;
    String scannerName;

    public Distances(Map<Beacon, Map<Beacon, Double>> d, String scannerName) {
        this.distancesByBeacon = d;
        this.scannerName = scannerName;
    }

    public Overlap findOverlap(Distances otherScanner) {
        Set<PairOfBeacons> results = new HashSet<>();
        Sample permanentSample = null;
        for (Beacon myBeacon : distancesByBeacon.keySet()) {
            Sample temporarySample = null;
            for (Beacon theirBeacon : otherScanner.distancesByBeacon.keySet()) {
                int matches = 0;
                // how many of THIS beacon's distances match up with the origin beacon's distance?
                for (Map.Entry<Beacon, Double> myDistance : distancesByBeacon.get(myBeacon).entrySet()) {
                    for (Map.Entry<Beacon, Double> theirDistance : otherScanner.distancesByBeacon.get(theirBeacon).entrySet()) {
                        if (Math.abs(theirDistance.getValue() - myDistance.getValue()) < 0.00005) {
                            Coordinates3d atob = myBeacon.coordinates.delta(myDistance.getKey().coordinates);
                            Coordinates3d ctod = theirBeacon.coordinates.delta(theirDistance.getKey().coordinates);

                            if (atob.isGoodBasis() && ctod.isGoodBasis()) {
                                temporarySample = new Sample(myBeacon, myDistance.getKey(), theirBeacon, theirDistance.getKey(), atob, ctod);
                            }

//                            System.out.println(myBeacon.name + "->" + myDistance.getKey().name + "=" + theirBeacon.name + "->" + theirDistance.getKey().name + "===" + myDistance.getValue());
                            matches++;
                        }
                    }
                }
                if (matches >= 11) {
//                    System.out.println(myBeacon.name + " has " + matches + " to " + theirBeacon.name + ".");
                    results.add(new PairOfBeacons(myBeacon, theirBeacon));
                    if (temporarySample != null) {
                        permanentSample = temporarySample;
                    }
                }
            }
        }
        return new Overlap(results, permanentSample);
    }

}

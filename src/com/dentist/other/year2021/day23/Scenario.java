package com.dentist.other.year2021.day23;

import java.util.*;
import java.util.stream.Collectors;

import static com.dentist.other.year2021.day23.Species.NOPE;

public class Scenario {
    public final int depth = 4;
    public final List<Move> provenance;
    Map<Amphipod, Coordinate> locationsByAmphipod = new HashMap<Amphipod, Coordinate>();
    Map<Coordinate, Amphipod> amphipodsByLocation = new HashMap<Coordinate, Amphipod>();
    Map<Integer, Amphipod> amphipodsByIndex = new HashMap<>();
    int totalCost = 0;

    public static final Coordinate LEFT_LEFT = new Coordinate(0, 0);
    public static final Coordinate LEFT = new Coordinate(0, 1);
    public static final Coordinate BETWEEN_ZERO_AND_ONE = new Coordinate(0, 3);
    public static final Coordinate BETWEEN_ONE_AND_TWO = new Coordinate(0, 5);
    public static final Coordinate BETWEEN_TWO_AND_THREE = new Coordinate(0, 7);
    public static final Coordinate RIGHT = new Coordinate(0, 9);
    public static final Coordinate RIGHT_RIGHT = new Coordinate(0, 10);

    public static final Set<Coordinate> VIABLE_TARGETS = Set.of(LEFT_LEFT, LEFT, BETWEEN_ZERO_AND_ONE, BETWEEN_ONE_AND_TWO, BETWEEN_TWO_AND_THREE, RIGHT, RIGHT_RIGHT);

    public Scenario(List<Move> moves, int cost, Coordinate... coords) {
        this.provenance = moves;
        this.totalCost = cost;

        for (int i = 0; i < coords.length; i++) {
            locationsByAmphipod.put(new Amphipod(getSpecies(i), i), coords[i]);
        }

        for (Map.Entry<Amphipod, Coordinate> e : locationsByAmphipod.entrySet()) {
            amphipodsByLocation.put(e.getValue(), e.getKey());
            amphipodsByIndex.put(e.getKey().index(), e.getKey());
        }
    }

    private Species getSpecies(int i) {
        if (i < 4) return Species.A;
        if (i < 8) return Species.B;
        if (i < 12) return Species.C;
        return Species.D;
    }

    public Set<Move> allViableMoves() {
        return amphipodsByIndex.values().stream().flatMap(a -> a.getViableMoves(this).stream()).collect(Collectors.toSet());
    }

    public Scenario applyMove(Move m) {
        Scenario s = makeACopy();
        s.locationsByAmphipod.remove(m.amphipod());
        s.locationsByAmphipod.put(m.amphipod(), m.destinationCoordinate());
        s.amphipodsByLocation.remove(m.origCoordinate());
        s.amphipodsByLocation.put(m.destinationCoordinate(), m.amphipod());
        s.totalCost += m.cost();
        s.provenance.add(m);
        return s;
    }

    private Scenario makeACopy() {
        Coordinate[] copiedCoordinates = new Coordinate[16];
        for (int i = 0; i < copiedCoordinates.length; i++) {
            copiedCoordinates[i] = locationsByAmphipod.get(amphipodsByIndex.get(i));
        }

        return new Scenario(new ArrayList<Move>(provenance), totalCost,
                copiedCoordinates);
    }

    private static final Amphipod BLANK = new Amphipod(NOPE, -1);

    private String write(Coordinate c) {
        return amphipodsByLocation.getOrDefault(c, BLANK).species().alias;
    }

    private String write(int row, int column) {
        return write(new Coordinate(row, column));
    }

    @Override
    public String toString() {
        return String.format("#############\r\n#%s%s.%s.%s.%s.%s%s#\r\n###%s#%s#%s#%s###\r\n  #%s#%s#%s#%s#  \n  #%s#%s#%s#%s#  \n  #%s#%s#%s#%s#  \r\n  #########  ",
                write(LEFT_LEFT), write(LEFT), write(BETWEEN_ZERO_AND_ONE), write(BETWEEN_ONE_AND_TWO), write(BETWEEN_TWO_AND_THREE), write(RIGHT), write(RIGHT_RIGHT),
                write(1, 2), write(1, 4), write(1, 6), write(1, 8),
                write(2, 2), write(2, 4), write(2, 6), write(2, 8),
                write(3, 2), write(3, 4), write(3, 6), write(3, 8),
                write(4, 2), write(4, 4), write(4, 6), write(4, 8));
    }

    public Scenario recurse(int depth, String parentString) {
        // if everyone is happy, then we return what we have here.
        boolean alreadySuccessful = amphipodsByIndex.values().stream().allMatch(amphipod -> amphipod.alreadyHappy(this));

        if (alreadySuccessful) {
            return this;
        }

        int previousCost = Integer.MAX_VALUE;
        Scenario currentWinner = null;

        Set<Move> children = allViableMoves();

        int move = 0;
        for (Move m : children) {
            Scenario child = applyMove(m);
//            System.out.println(m);
//            System.out.println("Depth = " + depth + ", move = " + move + ", parentString = " + parentString);
//            System.out.println(child);
//            System.out.println();

            Scenario eventualWinner = child.recurse(depth + 1, parentString.concat("," + Integer.toString(move)));
            if (eventualWinner != null && eventualWinner.totalCost < previousCost) {
                currentWinner = eventualWinner;
                previousCost = eventualWinner.totalCost;
//                System.out.println("Found one winner at " + parentString + " move " + move);
            }
            move++;
        }

        return currentWinner;
    }
}

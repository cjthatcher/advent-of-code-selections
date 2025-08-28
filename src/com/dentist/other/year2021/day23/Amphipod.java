package com.dentist.other.year2021.day23;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public record Amphipod(Species species, int index) {

    @Override
    public String toString() {
        return String.format("%s:%s", species, index);
    }

    public Set<Move> getViableMoves(Scenario s) {
        // Am I already where I want to be?
        Coordinate currentLocation = s.locationsByAmphipod.get(this);

        if (alreadyAtDestination(currentLocation, s)) {
            return Collections.emptySet();
        }

        Set<Coordinate> coordinatesIShouldTryToReach = new HashSet<>();

        Optional<Coordinate> destination = finalDestinationAvailable(s);
        if (destination.isPresent() && canReach(destination.get(), s)) {
            return Collections.singleton(buildMove(destination.get(), currentLocation));
        }

        // if I am in a side room, I should try to reach ALL the intermediate spots.
        if (currentLocation.row() > 0) {
            coordinatesIShouldTryToReach.addAll(Scenario.VIABLE_TARGETS);
        }

        return coordinatesIShouldTryToReach.stream().filter(target -> canReach(target, s)).map(target -> buildMove(target, currentLocation)).collect(Collectors.toSet());
    }

    private Move buildMove(Coordinate target, Coordinate currentLocation) {
        return new Move(this, currentLocation, target, walkDistance(currentLocation, target) * species.cost);
    }

    private int walkDistance(Coordinate currentLocation, Coordinate target) {
        int walk = 0;
        boolean sameColumn = currentLocation.column() == target.column();
        if (!sameColumn) {
            walk += currentLocation.row() + target.row();
        }
        walk += Math.abs(currentLocation.column() - target.column());
        return walk;
    }

    boolean canReach(Coordinate target, Scenario s) {
        Coordinate step = s.locationsByAmphipod.get(this);
        if (s.amphipodsByLocation.get(target) != null && !s.amphipodsByLocation.get(target).equals(this)) {
            return false;
        }

        while (!step.equals(target)) {
            // If there's someone in this spot, and that person is not me, I can't get where I'm going.
            if (s.amphipodsByLocation.get(step) != null && s.amphipodsByLocation.get(step).index != this.index) {
                return false;
            }

            // If you're at the correct column, try stepping down into the slot.
            if (step.column() == target.column()) {
                step = new Coordinate(step.row() + 1, step.column());
            }

            // If you're not in the hallway, and you're not lined up with your target, you need to try to step up into the hallway.
            if (step.column() != target.column() && step.row() > 0) {
                step = new Coordinate(step.row() - 1, step.column());
            }

            // If you're in the hallway, and your target is to your right, try stepping to the right.
            if (step.row() == 0 && target.column() > step.column()) {
                step = new Coordinate(step.row(), step.column() + 1);
            }

            // If you're in the hallway, and your target is to your left, try stepping to your left.
            if (step.row() == 0 && target.column() < step.column()) {
                step = new Coordinate(step.row(), step.column() - 1);
            }
        }
        return true;
    }


    /**
     * If the two slots are empty, I should target the bottom slot.
     * If the bottom slot is filled with a friend, and top is empty, I should target the top slot.
     * Otherwise don't target either.
     */
    private Optional<Coordinate> finalDestinationAvailable(Scenario s) {
        // if all empty, I can go to the bottom.
        // if everybody is my species up till the next available spot, I'll snag it.

        //These start from the top. empty[0] represents the hallway. empty[4] is the bottom one.

        boolean[] empty = new boolean[s.depth + 1];
        boolean[] mySpecies = new boolean[s.depth + 1];

        for (int row = s.depth; row > 0; row--) {
            empty[row] = s.amphipodsByLocation.get(new Coordinate(row, species.targetColumn)) == null;
            mySpecies[row] = s.amphipodsByLocation.get(new Coordinate(row, species.targetColumn)) != null && s.amphipodsByLocation.get(new Coordinate(row, species.targetColumn)).species == this.species;
        }

        // For each coordinate
        for (int row = s.depth; row > 0; row--) {
            // Start at row == 4

            boolean okayBelow = true;
            for (int probeRowDescending = row + 1; probeRowDescending <= s.depth; probeRowDescending++) {
                if (mySpecies[probeRowDescending] == false) {
                    okayBelow = false;
                    break;
                }
            }

            boolean okayAbove = true;
            for (int probeRowAscending = row; probeRowAscending > 0; probeRowAscending--) {
                if (empty[probeRowAscending] == false) {
                    okayAbove = false;
                    break;
                }
            }

            if (okayAbove && okayBelow) {
                return Optional.of(new Coordinate(row, species.targetColumn));
            }
        }

        return Optional.empty();
    }

    private boolean alreadyAtDestination(Coordinate currentLocation, Scenario s) {
        // everyone below you is your species.
        // everyone above you is your species or empty.
        // you are at your target column.

        if (currentLocation.column() != species.targetColumn) {
            return false;
        }

        for (int rowBelowMe = currentLocation.row() + 1; rowBelowMe <= s.depth; rowBelowMe++) {
            Amphipod roommate = s.amphipodsByLocation.get(new Coordinate(rowBelowMe, currentLocation.column()));
            if (roommate == null || roommate.species != this.species) {
                return false;
            }
        }

        for (int rowAboveMe = currentLocation.row() - 1; rowAboveMe > 0; rowAboveMe--) {
            Amphipod roommate = s.amphipodsByLocation.get(new Coordinate(rowAboveMe, currentLocation.column()));
            // must be null OR my species
            if (roommate != null && roommate.species != this.species) {
                return false;
            }
        }

        return true;
    }

    public boolean alreadyHappy(Scenario s) {
        return alreadyAtDestination(s.locationsByAmphipod.get(this), s);
    }

}

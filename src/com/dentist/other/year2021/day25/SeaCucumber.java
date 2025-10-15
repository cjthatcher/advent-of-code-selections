package com.dentist.other.year2021.day25;

import com.dentist.other.year2021.day23.Coordinate;

public class SeaCucumber {

    /** We will do two passes when we evolve. The first pass only does RIGHT. The second pass only does DOWN. */
    public static void evolveSpace(Coordinate here, SeaFloor currentGeneration, SeaFloor nextGeneration, int pass) {
        // Where does this space want to go? What is currently there?
        // Is it possible that someone with higher priority is going to fill that space for me?
        // first east, then south.

        SeaSpace occupant = currentGeneration.floor.get(here);
        Coordinate target = currentGeneration.wrap(here.add(occupant.target));
        SeaSpace targetCurrentOccupant = currentGeneration.floor.get(target);
        SeaSpace targetNextGenOccupant = nextGeneration.floor.get(target);

        if (occupant == SeaSpace.RIGHT) {
            if (pass != 0) {
                return;
            }
            if (targetCurrentOccupant == SeaSpace.EMPTY) {
                // I can move to the right. Let's do it.
                nextGeneration.floor.put(target, occupant); // next gen has this cucumber in it.
                nextGeneration.floor.put(here, SeaSpace.RIGHT_EMPTY); // for now, next gen has this space empties.
            } else {
                nextGeneration.floor.put(here, occupant);
            }
        } else if (occupant == SeaSpace.DOWN) {
            if (pass != 1) {
                return;
            }
            // If the target space was just evacuated by a RIGHT...
            if (targetNextGenOccupant == SeaSpace.RIGHT_EMPTY && targetCurrentOccupant == SeaSpace.RIGHT) {
                nextGeneration.floor.put(target, occupant);
                nextGeneration.floor.put(here, SeaSpace.EMPTY);
            }
            // If the target space is currently empty, AND it's not about to be filled by a RIGHT
            else if (targetCurrentOccupant == SeaSpace.EMPTY && targetNextGenOccupant == null) {
                nextGeneration.floor.put(target, occupant);
                nextGeneration.floor.put(here, SeaSpace.EMPTY);
            }
            else {
                nextGeneration.floor.put(here, occupant);
            }
        }
    }
}

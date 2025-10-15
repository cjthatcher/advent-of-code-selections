package com.dentist.other.year2021.day25;

import com.dentist.other.year2021.day23.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeaFloor {
    final int maxColumn;
    final int maxRow; // This needs to be one past the final row index. So if there are 10 rows, we'll be zero based, and rows 0-9 will be legit. So this value needs to be 10 in that case.
    final int rights;
    final int downs;

    Map<Coordinate, SeaSpace> floor = new HashMap<>();

    public SeaFloor(int maxColumn, int maxRow, int rights, int downs) {
        this.maxColumn = maxColumn;
        this.maxRow = maxRow;
        this.rights = rights;
        this.downs = downs;
    }

    public static SeaFloor fromInput(List<String> lines) {
        Map<Coordinate, SeaSpace> tempFloor = new HashMap<>();
        int tempMaxColumn = 0;
        int row = 0;
        int downCount = 0;
        int rightCount = 0;
        for (String line : lines) {
            int column = 0;
            for (char c : line.toCharArray()) {
                Coordinate here = new Coordinate(row, column++);
                if (c == 'v') {
                    tempFloor.put(here, SeaSpace.DOWN);
                    downCount++;
                } else if (c == '>') {
                    tempFloor.put(here, SeaSpace.RIGHT);
                    rightCount++;
                } else if (c == '.') {
                    tempFloor.put(here, SeaSpace.EMPTY);
                } else {
                    System.err.println("HEY< PARSED IT WRONG!");
                }
            }
            tempMaxColumn = column;
            row++;
        }

        SeaFloor result = new SeaFloor(tempMaxColumn, row, rightCount, downCount);
        result.floor = tempFloor;
        return result;
    }

    public Coordinate wrap(Coordinate original) {
        return new Coordinate(original.row() % (maxRow), original.column() % (maxColumn));
    }

    public SeaFloor step() {
        SeaFloor nextGeneration = new SeaFloor(this.maxColumn, this.maxRow, this.rights, this.downs);

        for (int pass = 0; pass < 2; pass++) {
            for (int row = 0; row < maxRow; row++) {
                for (int column = 0; column < maxColumn; column++) {
                    Coordinate space = new Coordinate(row, column);
                    SeaCucumber.evolveSpace(space, this, nextGeneration, pass);
                }
            }
        }

        nextGeneration.ensureAndFill();
        return nextGeneration;
    }

    private void ensureAndFill() {
        // verify that we have the right number of RIGHTS, the right number of DOWNS, and fill the rest as empty.
        int countedRights = 0;
        int countedDowns = 0;

        for (Coordinate c : allMapSpaces()) {
            floor.putIfAbsent(c, SeaSpace.EMPTY);
            SeaSpace current = floor.get(c);
            if (current == SeaSpace.DOWN) {
                countedDowns++;
            } else if (current == SeaSpace.RIGHT) {
                countedRights++;
            } else if (current == SeaSpace.RIGHT_EMPTY) {
                floor.put(c, SeaSpace.EMPTY);
            }
        }

        if (countedRights != rights || countedDowns != downs) {
            System.err.println("AH, WE EVOLVED WRONG!");
        }
    }

    private List<Coordinate> allMapSpaces() {
        List<Coordinate> allCoordinates = new ArrayList<Coordinate>(maxColumn * maxRow);
        for (int row = 0; row < maxRow; row++) {
            for (int column = 0; column < maxColumn; column++) {
                allCoordinates.add(new Coordinate(row, column));
            }
        }

        return allCoordinates;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < maxRow; row++) {
            for (int column = 0; column < maxColumn; column++) {
                SeaSpace current = floor.get(new Coordinate(row, column));
                sb.append(current == SeaSpace.EMPTY ? "." : current == SeaSpace.RIGHT ? ">" : "v");
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    public boolean isDifferent(SeaFloor oldGeneration) {
        for (Coordinate c : allMapSpaces()) {
            if (oldGeneration.floor.get(c) != this.floor.get(c)) {
                return true;
            }
        }
        return false;
    }
}

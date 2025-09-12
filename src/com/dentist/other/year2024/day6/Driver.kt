package com.dentist.other.year2024.day6

import com.dentist.other.year2021.day23.CardinalDirection
import com.dentist.other.year2021.day23.Coordinate
import java.io.File

fun main(args: Array<String>) {
    val lines = File("resources/2024/6.txt").readLines()

    val (obstacles, traveler, bottomRightCorner) = lines.foldIndexed(
        BoardFeatures(mutableSetOf(), null, Coordinate(0, 0))
    ) { row, obstacles, line ->
        addObstaclesToSet(
            obstacles,
            row,
            line
        )
    }

    val distinctLocations = traveler?.distinctLocationsTillExit(obstacles.toSet(), bottomRightCorner)
    println(String.format("The traveler will visit %d distinct locations", distinctLocations))

    var loops = 0;
    for (row in 0..bottomRightCorner.row) {
        for (column in 0..bottomRightCorner.column) {
            val probeCoordinate = Coordinate(row, column)
            if (!obstacles.contains(probeCoordinate) && traveler?.stuckInloop(obstacles, probeCoordinate,bottomRightCorner) == true) {
                loops++;
            }
        }
    }
    println(String.format("There are %d distinct obstacles we can use to create loops.", loops))

}

private fun addObstaclesToSet(features: BoardFeatures, row: Int, line: String): BoardFeatures {
    var greatestColumn = 0
    line.toCharArray().forEachIndexed { column, character ->
        if (character == '#') features.obstacles.add(Coordinate(row, column))
        if (character == '^') features.traveler = Traveler(Coordinate(row, column), CardinalDirection.UP)
        if (character == 'v') features.traveler = Traveler(Coordinate(row, column), CardinalDirection.DOWN)
        if (character == '<') features.traveler = Traveler(Coordinate(row, column), CardinalDirection.LEFT)
        if (character == '>') features.traveler = Traveler(Coordinate(row, column), CardinalDirection.RIGHT)
        greatestColumn = column
    }
    features.bottomRightCorner = Coordinate(row, greatestColumn)
    return features
}

data class BoardFeatures(
    val obstacles: MutableSet<Coordinate>,
    var traveler: Traveler?,
    var bottomRightCorner: Coordinate
)

data class Collision(val obstacle: Coordinate, val directionOfOrigin: CardinalDirection)

class Traveler(private val startingPosition: Coordinate, private val startingDirection: CardinalDirection) {

    fun distinctLocationsTillExit(obstacles: Set<Coordinate>, bottomRightCorner: Coordinate): Int {
        val distinctStepSet = mutableSetOf<Coordinate>()
        var currentPosition = startingPosition;
        var currentDirection = startingDirection;
        while (inBounds(currentPosition, bottomRightCorner)) {
            distinctStepSet.add(currentPosition)
            val nextStep = currentPosition.add(currentDirection.movement)
            if (obstacles.contains(nextStep)) {
                currentDirection = currentDirection.turnRight()
                continue;
            } else {
                currentPosition = nextStep;
            }
        }
        return distinctStepSet.size
    }

    fun stuckInloop(obstacles: Set<Coordinate>, additionalObstacle: Coordinate, bottomRightCorner: Coordinate): Boolean {
        var currentPosition = startingPosition;
        var currentDirection = startingDirection;
        val collisions = mutableSetOf<Collision>()
        while (inBounds(currentPosition, bottomRightCorner)) {
            val nextStep = currentPosition.add(currentDirection.movement)
            // If we hit the same obstacle coming from the same direction as before, we know we're in a loop.
            if (obstacles.contains(nextStep) || additionalObstacle == nextStep) {
                val collision = Collision(nextStep, currentDirection)
                if (collisions.contains(collision)) {
                    return true
                } else {
                    collisions.add(collision)
                }
                currentDirection = currentDirection.turnRight()
                continue;
            } else {
                currentPosition = nextStep;
            }
        }
        return false
    }

    private fun inBounds(currentPosition: Coordinate, bottomRightCorner: Coordinate): Boolean {
        return currentPosition.row >= 0 &&
                currentPosition.row <= bottomRightCorner.row &&
                currentPosition.column >= 0 &&
                currentPosition.column <= bottomRightCorner.column
    }
}

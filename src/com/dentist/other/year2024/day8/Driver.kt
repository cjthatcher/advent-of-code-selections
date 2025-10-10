package com.dentist.other.year2024.day8

import com.dentist.other.year2021.day23.Coordinate
import java.io.File

fun main(args:Array<String>) {

    // a map<char, set<coordinate>>
    // for each pair of coordinates for a given char, make their two antinodes. Filter out antinodes that are outside of the range.

    val lines = File("resources/2024/8.txt").readLines()
    val radioTowers = HashMap<Char, MutableSet<Coordinate>>()
    var row = 0
    var maxColumn = 0
    var maxRow = 0
    for (line in lines) {
        var column = 0
        for (c in line.toCharArray()) {
            if (c != '.') {
                radioTowers.putIfAbsent(c, mutableSetOf<Coordinate>())
                radioTowers.get(c)?.add(Coordinate(row, column))
            }
            if (column > maxColumn) {
                maxColumn = column
            }
            column++
        }
        if (row > maxRow) {
            maxRow = row
        }
        row++
    }

    val antinodes = mutableSetOf<Coordinate>()

    fun inBounds(a: Coordinate): Boolean =
        a.row >= 0 && a.row <= maxRow && a.column >= 0 && a.column <= maxColumn

    for (c in radioTowers.keys) {
        val pairs = generatePairs(radioTowers.getOrDefault(c, setOf<Coordinate>()))
        pairs.flatMap { generateComplexAntinodes(it.first, it.second) }.filter { inBounds(it) }.forEach { antinodes.add(it) }
    }

    println("Here's what the map looks like!")
    for (row in 0..maxRow) {
        var rowString = ""
        for (column in 0..maxColumn) {
            var toWrite = '.'
            val here = Coordinate(row, column)

            for(c in radioTowers.keys) {
                if (radioTowers.getOrDefault(c, setOf<Coordinate>()).contains(here)) {
                    toWrite = c
                    break
                }
            }

            if (antinodes.contains(here)) {
                toWrite = '#'
            }
            rowString = "$rowString$toWrite"
        }
        println(rowString)
    }

    println("Max row = $maxRow and maxColumn = $maxColumn")

    println("There are ${antinodes.size} unique antinodes within the map")
}

fun generateSimpleAntinodes(towerA: Coordinate, towerB: Coordinate): Set<Coordinate> {
    val deltaA = towerA.delta(towerB)
    val antinodeA = towerB.add(deltaA)
    val deltaB = towerB.delta(towerA)
    val antinodeB = towerA.add(deltaB)
    return setOf(antinodeA, antinodeB)
}

fun generateComplexAntinodes(towerA: Coordinate, towerB: Coordinate): Set<Coordinate> {
    // We want every point that perfectly lines up  on the map.
    // So, we need to simplify the vector between the two towers.
    val antinodes = mutableSetOf<Coordinate>()
    
    val forward = towerA.delta(towerB).simplifyVector()
    var here = towerA
    while (inBounds(here, 49)) {
        antinodes.add(here)
        here = here.add(forward)
    }

    val backwards = towerB.delta(towerA).simplifyVector()
    here = towerB
    while(inBounds(here, 49)) {
        antinodes.add(here)
        here = here.add(backwards)
    }
    return antinodes
}

fun inBounds(coordinate: Coordinate, maxDimension: Int) = coordinate.row >= 0 && coordinate.row <= maxDimension && coordinate.column >= 0 && coordinate.column <= maxDimension

fun generatePairs(coordinates: Set<Coordinate>): Set<Pair<Coordinate, Coordinate>> {
    val result = mutableSetOf<Pair<Coordinate, Coordinate>>()
    for (towerA in coordinates) {
        for (towerB in coordinates) {
            if (towerA.equals(towerB)) {
                continue
            }
            if (!result.contains(Pair<Coordinate, Coordinate>(towerB, towerA))) {
                result.add(Pair<Coordinate, Coordinate>(towerA, towerB))
            }
        }
    }
    return result
}

package com.dentist.other.year2024.day10

import com.dentist.other.year2021.day23.Coordinate
import java.io.File

fun main(args: Array<String>) {
    val lines = File("resources/2024/10.txt").readLines()
    val topography = buildTopo(lines)
    var score = 0
    for (mountain in topography.topo.values) {
        score += mountain.findUniquePathsToSummits(topography).size
    }
    println("The rank is $score")
}

fun buildTopo(strings: List<String>): Topography {
    val mapBuilder = mutableMapOf<Coordinate, Mountain>()
    var r = 0
    for (row in strings) {
        var c = 0
        for (column in row.toCharArray()) {
            val coordinate = Coordinate(r, c)
            val mountain = Mountain(coordinate, column.toString().toInt())
            mapBuilder.put(coordinate, mountain)
            c++
        }
        r++
    }
    return Topography(mapBuilder.toMap())
}

data class Topography(val topo: Map<Coordinate, Mountain>) {
    fun neighbors(here: Mountain): Set<Mountain> {
        return listOfNotNull(
            topo[here.location.up()],
            topo[here.location.down()],
            topo[here.location.left()],
            topo[here.location.right()]
        ).toSet()
    }
}

data class Mountain(val location: Coordinate, val height: Int) {
    fun isTrailhead() = height == 0
    fun isSummit() = height == 9
    fun validNeighbors(topo: Topography): Set<Mountain> {
        return topo.neighbors(this).filter { neighbor -> neighbor.height - 1 == height }.toSet()
    }

    fun findUniquePathsToSummits(topo: Topography): Set<List<Coordinate>> {
        if (!isTrailhead()) {
            return setOf()
        } else {
            val result = mutableSetOf<List<Coordinate>>()
            recurseForUniquePaths(topo, listOf(location), result)
            return result
        }
    }

    fun recurseForUniquePaths(topo: Topography, soFar: List<Coordinate>, winners: MutableSet<List<Coordinate>>) {
        val neighbors = validNeighbors(topo)
            for (m in neighbors) {
                if (m.isSummit()) {
                    winners.add(soFar.plus(m.location))
                } else {
                    m.recurseForUniquePaths(topo, soFar.plus(m.location), winners)
                }
            }
    }

    fun findUniqueSummits(topo: Topography): Set<Mountain> {
        if (!isTrailhead()) {
            return setOf()
        } else {
            return recurseForUniqueSummits(topo, mutableSetOf())
        }
    }

    private fun recurseForUniqueSummits(topo: Topography, summits: MutableSet<Mountain>): Set<Mountain> {
        val neighbors = validNeighbors(topo)
        for (m in neighbors) {
            if (m.isSummit()) {
                summits.add(m)
            } else {
                m.recurseForUniqueSummits(topo, summits)
            }
        }
        return summits.toSet()
    }
}


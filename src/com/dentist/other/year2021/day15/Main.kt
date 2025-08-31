package com.dentist.other.year2021.day15

import java.io.File
import java.util.*
import kotlin.math.min

fun main(args: Array<String>) {
    val mapOfDeath = Day15.FifteenFileConverter.toBigMap("resources/2021/15.txt")
    val dangerMap = Day15.DangerMap(mapOfDeath)
    Day15(dangerMap).solve()
}

class Day15(val dangerMap: DangerMap) {

    //Priority Queue of {distance, location}
    val sortedDistances = TreeSet<Pair<Int, Pair<Int, Int>>> { a, b ->
        if (a.first != b.first) {
            a.first - b.first
        } else if (a.second.first != b.second.first) {
            a.second.first - b.second.first
        } else {
            a.second.second - b.second.second
        }
    }

    private val tempDistances = hashMapOf<Pair<Int, Int>, Int>()
    private val visitedSet = hashSetOf<Pair<Int, Int>>()

    fun solve() {
        println("${dangerMap.findDestinationPair()}, ${dangerMap.getRiskAt(dangerMap.findDestinationPair())}")
        dangerMap.printMap()
        dangerMap.printAFewSamples()
        dangerMap.keys.forEach { sortedDistances.add(Integer.MAX_VALUE to it) }
        sortedDistances.remove(Integer.MAX_VALUE to (0 to 0))
        tempDistances[0 to 0] = 0
        sortedDistances.add(0 to (0 to 0))
        val destination = dangerMap.findDestinationPair()
        while (destination !in visitedSet) {
            val target = sortedDistances.first().second
            updateNeighborsOfTarget(target)
            setTargetToVisited(target)
        }

        println("Distance to target = ${tempDistances[destination]}")
    }


    private fun updateNeighborsOfTarget(
        target: Pair<Int, Int>
    ) {
        val distanceToTarget = tempDistances[target] ?: 0
        dangerMap.getNeighbors(target).filter { it !in visitedSet }.forEach {
            val neighbor = it
            val currentDistance = tempDistances[neighbor] ?: Integer.MAX_VALUE
            val cost = dangerMap.getRiskAt(neighbor)
            val replacementDistance = min(currentDistance, distanceToTarget + cost)
            sortedDistances.remove(currentDistance to neighbor)
            sortedDistances.add(replacementDistance to neighbor)
            tempDistances[neighbor] = replacementDistance
        }
        println("sortedDistances.size = ${sortedDistances.size}")
    }

    private fun setTargetToVisited(
        target: Pair<Int, Int>,
    ) {
        visitedSet.add(target)
        sortedDistances.remove(tempDistances[target] to target)
        println("Visited $target; distance ${tempDistances[target]}; visited: ${visitedSet.size}; unvisited: ${sortedDistances.size}")
    }

    class FifteenFileConverter {
        companion object {
            fun toMap(fileName: String): Map<Pair<Int, Int>, Int> =
                File(fileName).readLines().flatMapIndexed { rowIndex, line ->
                    line.toCharArray().mapIndexed { columnIndex, character ->
                        (rowIndex to columnIndex) to Integer.parseInt("" + character)
                    }
                }.toMap()

            fun toBigMap(fileName: String): Map<Pair<Int, Int>, Int> {
                val oldMap = toMap(fileName)
                val freshMap = mutableMapOf<Pair<Int, Int>, Int>()
                val rowCount = oldMap.keys.maxOfOrNull { it.first + 1 } ?: 0
                val columnCount = oldMap.keys.maxOfOrNull { it.second + 1 } ?: 0
                for (rowMultiplier in 0..4) {
                    for (columnMultiplier in 0..4) {
                        val rowOffset = rowMultiplier * rowCount
                        val columnOffset = columnMultiplier * columnCount
                        oldMap.forEach { oldEntry ->
                            val rawValue = oldEntry.value + rowMultiplier + columnMultiplier
                            val goodValue = if (rawValue >= 10) rawValue - 9 else rawValue
                            freshMap[oldEntry.key.first + rowOffset to oldEntry.key.second + columnOffset] =
                                goodValue
                        }
                    }
                }
                return freshMap.toMap()
            }
        }
    }

    class DangerMap(val map: Map<Pair<Int, Int>, Int>) {

        val keys
            get() = map.keys

        fun findDestinationPair(): Pair<Int, Int> {
            return map.keys.maxByOrNull { it.first + it.second } ?: 0 to 0
        }

        fun getRiskAt(location: Pair<Int, Int>): Int = map[location] ?: Integer.MAX_VALUE

        fun getNeighbors(location: Pair<Int, Int>): List<Pair<Int, Int>> {
            val up = location.first - 1 to location.second
            val down = location.first + 1 to location.second
            val left = location.first to location.second - 1
            val right = location.first to location.second + 1

            return listOf(up, down, left, right).filter(map::containsKey)
        }

        fun printMap() {
            val rows = findDestinationPair().first
            val columns = findDestinationPair().second
            for (i in 0..rows) {
                for (j in 0..columns) {
                    print(map[i to j])
                }
                println()
            }
        }

        fun printAFewSamples() {
            println(
                "sampling:" +
                        " (1,1) -> ${map[1 to 1]};" +
                        " (1,101) -> ${map[1 to 101]};" +
                        " (1,201) -> ${map[1 to 201]};" +
                        " (1,301) -> ${map[1 to 301]};" +
                        " (1,401) -> ${map[1 to 401]};"
            )
            println(
                "sampling:" +
                        " (1,1) -> ${map[1 to 1]};" +
                        " (101,1) -> ${map[101 to 1]};" +
                        " (201,1) -> ${map[201 to 1]};" +
                        " (301,1) -> ${map[301 to 1]};" +
                        " (401,1) -> ${map[401 to 1]};"
            )
            println(
                "sampling:" +
                        " (1,1) -> ${map[1 to 1]};" +
                        " (101,101) -> ${map[101 to 101]};" +
                        " (201,201) -> ${map[201 to 201]};" +
                        " (301,301) -> ${map[301 to 301]};" +
                        " (401,401) -> ${map[401 to 401]};"
            )
            println(
                "sampling:\n" +
                        " (1,1) -> ${map[1 to 1]};" +
                        " (1,101) -> ${map[1 to 101]};" +
                        " (1,201) -> ${map[1 to 201]};" +
                        " (1,301) -> ${map[1 to 301]};" +
                        " (1,401) -> ${map[1 to 401]};\n" +
                        " (101,1) -> ${map[101 to 1]};" +
                        " (101,101) -> ${map[101 to 101]};" +
                        " (101,201) -> ${map[101 to 201]};" +
                        " (101,301) -> ${map[101 to 301]};" +
                        " (101,401) -> ${map[101 to 401]};\n" +
                        " (201,1) -> ${map[201 to 1]};" +
                        " (201,101) -> ${map[201 to 101]};" +
                        " (201,201) -> ${map[201 to 201]};" +
                        " (201,301) -> ${map[201 to 301]};" +
                        " (201,401) -> ${map[201 to 401]};\n" +
                        " (301,1) -> ${map[301 to 1]};" +
                        " (301,101) -> ${map[301 to 101]};" +
                        " (301,201) -> ${map[301 to 201]};" +
                        " (301,301) -> ${map[301 to 301]};" +
                        " (301,401) -> ${map[301 to 401]};\n" +
                        " (401,1) -> ${map[401 to 1]};" +
                        " (401,101) -> ${map[401 to 101]};" +
                        " (401,201) -> ${map[401 to 201]};" +
                        " (401,301) -> ${map[401 to 301]};" +
                        " (401,401) -> ${map[401 to 401]};"
            )
        }
    }
}
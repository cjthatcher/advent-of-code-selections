import java.io.File
import java.util.*

fun main() {
    val octopusMap = hashMapOf<Pair<Int, Int>, Octopus>()
    File("resources/2021/11.txt").readLines().map { it.toCharArray() }
        .forEachIndexed { row, charArray ->
            charArray.forEachIndexed { column, char ->
                val coordinates = Pair(row, column)
                octopusMap.put(
                    coordinates,
                    Octopus(coordinates, toInt(char), false)
                )
            }
        }

    var flashCount = 0
    printMap(octopusMap)
    for (round in 1..10_000) {
        startRound(octopusMap)
        flashCount += doFlashes(octopusMap)
        val allFlashed = didWeAllFlash(octopusMap)
        if (allFlashed) {
            println("Round $round: We all flashed.")
            return
        }
        refreshOctopi(octopusMap);
        printMap(octopusMap)
    }
    println("Hey, flashCount = $flashCount");
}

fun didWeAllFlash(octopusMap: HashMap<Pair<Int, Int>, Octopus>): Boolean =
    octopusMap.values.find { it.hasFlashed == false } == null


fun refreshOctopi(octopusMap: HashMap<Pair<Int, Int>, Octopus>) =
    octopusMap.values.forEach(Octopus::refresh)


fun printMap(octoMap: Map<Pair<Int, Int>, Octopus>) {
    for (row in 0..9) {
        for (column in 0..9) {
            print(octoMap.get(Pair(row, column))?.energyLevel)
        }
        println()
    }
}

fun startRound(octopusMap: HashMap<Pair<Int, Int>, Octopus>) {
    for (octopus in octopusMap.values) {
        octopus.addOneEnergy()
    }
}

fun doFlashes(octopusMap: HashMap<Pair<Int, Int>, Octopus>): Int {
    var flashCount = 0
    do {
        var somebodyFlashed = false;
        for (octopus in octopusMap.values) {
            if (!octopus.hasFlashed && octopus.energyLevel > 9) {
                octopus.flash(octopusMap)
                somebodyFlashed = true;
                flashCount++
            }
        }
    } while (somebodyFlashed == true)
    return flashCount
}


fun toInt(char: Char): Int =
    when (char) {
        '0' -> 0
        '1' -> 1
        '2' -> 2
        '3' -> 3
        '4' -> 4
        '5' -> 5
        '6' -> 6
        '7' -> 7
        '8' -> 8
        '9' -> 9
        else -> 0
    }


data class Octopus(val coordinates: Pair<Int, Int>, var energyLevel: Int, var hasFlashed: Boolean) {
    fun neighborCoordinates(): Set<Pair<Int, Int>> {
        val x = coordinates.first;
        val y = coordinates.second;
        return setOf(
            Pair(x - 1, y - 1),
            Pair(x - 1, y),
            Pair(x - 1, y + 1),
            Pair(x, y - 1),
            Pair(x, y + 1),
            Pair(x + 1, y - 1),
            Pair(x + 1, y),
            Pair(x + 1, y + 1)
        ).filter { it.first >= 0 && it.first <= 9 && it.second >= 0 && it.second <= 9 }.toSet()
    }

    fun neighbors(octoMap: Map<Pair<Int, Int>, Octopus>): Set<Octopus?> =
        neighborCoordinates().map { octoMap.get(it) }.toSet()

    fun refresh() {
        if (hasFlashed) {
            energyLevel = 0
            hasFlashed = false;
        }
    }

    fun addOneEnergy() {
        energyLevel += 1
    }

    fun flash(octopusMap: HashMap<Pair<Int, Int>, Octopus>) {
        hasFlashed = true
        neighbors(octopusMap).forEach { it?.addOneEnergy() }
    }
}
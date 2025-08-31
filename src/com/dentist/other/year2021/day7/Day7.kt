import java.io.File
import java.lang.Math.abs

fun main(args: Array<String>) {
    val horizontalPositions =
        File("resources/2021/7.txt").readLines().first().split(",").map { Integer.parseInt(it) }.sorted()
    alignCrabsSimple(horizontalPositions)
    alignCrabsGeometric(horizontalPositions)
}

fun alignCrabsGeometric(horizontalPositions: List<Int>) {
    println("horizontal positions = $horizontalPositions")
    var bestPair = Pair(-1, Int.MAX_VALUE)
    for (position in IntRange(horizontalPositions.first(), horizontalPositions.last())) {
        val costToPosition = moveToWithGeometricDistance(horizontalPositions, position)
        println("cost to $position is $costToPosition")
        if (costToPosition < bestPair.second) {
            bestPair = Pair(position, costToPosition)
        }
    }
    println("Best position = $bestPair")
}

private fun alignCrabsSimple(horizontalPositions: List<Int>) {
    println("horizontal positions = $horizontalPositions")
    val median = horizontalPositions.get(horizontalPositions.size / 2)
    println("median = $median")
    val costToPosition = moveTo(horizontalPositions, median);
    println("cost to $median = $costToPosition")
}

fun moveTo(horizontalPositions: List<Int>, median: Int): Int = horizontalPositions.map { abs(it - median) }.sum()

fun moveToWithGeometricDistance(horizontalPositions: List<Int>, position: Int): Int =
    horizontalPositions.map { geometricDistance(abs(it - position)) }.sum()

val memoizedDistances = hashMapOf<Int, Int>()

fun geometricDistance(destination: Int): Int {
    if (memoizedDistances.containsKey(destination)) {
        return memoizedDistances.getOrDefault(destination, -1)
    } else {
        if (destination == 0) {
            return 0
        }
        val recursiveDistance = geometricDistance(destination - 1) + destination
        memoizedDistances.put(destination, recursiveDistance)
        return recursiveDistance
    }
}
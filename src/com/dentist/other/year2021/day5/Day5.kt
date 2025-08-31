import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main(args: Array<String>) {
    val occurrenceMap = mutableMapOf<Point, Int>()
    File("resources/2021/5.txt").readLines().map { line -> LineSegment.fromString(line) }
        .filter { it.hasBoringSlope() }.flatMap { it.generatePoints() }
        .forEach { occurrenceMap[it] = occurrenceMap.getOrDefault(it, 0) + 1 }

    val count = occurrenceMap.values.count { it >= 2 }

    println("count: $count")
}

data class LineSegment(val p1: Point, val p2: Point) {
    companion object {
        fun fromString(line: String): LineSegment {
            val parts = line.split("->")
            return LineSegment(Point.fromString(parts[0]), Point.fromString(parts[1]))
        }
    }

    fun hasBoringSlope(): Boolean {
        return isVertical().or(isHorizontal()).or(isDiagonal())
    }

    fun generatePoints(): Set<Point> {
        return buildSet {
            add(p1)
            add(p2)
            if (isVertical()) {
                for (y in IntRange(min(p1.y, p2.y), max(p1.y, p2.y))) {
                    add(Point(p1.x, y))
                }
            }
            if (isHorizontal()) {
                for (x in IntRange(min(p1.x, p2.x), max(p1.x, p2.x))) {
                    add(Point(x, p1.y))
                }
            }
            if (isDiagonal()) { //diagonal baby
                val leftPoint = if (p1.x < p2.x) p1 else p2
                val rightPoint = if (leftPoint == p1) p2 else p1
                val distance = rightPoint.x - leftPoint.x

                if (leftPoint.y < rightPoint.y) { //Going up!
                    for (i in 0..distance) {
                        add(Point(leftPoint.x + i, leftPoint.y + i))
                    }
                } else if (rightPoint.y < leftPoint.y) { //Going down!
                    for (i in 0..distance) {
                        add(Point(leftPoint.x + i, leftPoint.y - i))
                    }
                }
            }
        }
    }

    private fun isDiagonal() = abs(p1.y.minus(p2.y)) == abs(p1.x.minus(p2.x))

    private fun isHorizontal() = p1.y == p2.y

    private fun isVertical() = p1.x == p2.x
}


data class Point(val x: Int, val y: Int) {
    companion object {
        fun fromString(s: String): Point {
            val parts = s.trim().split(",")
            return Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]))
        }
    }
}
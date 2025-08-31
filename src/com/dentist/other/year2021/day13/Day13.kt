import java.io.File

fun main(args: Array<String>) {
    Day13.Problem.fromFile("resources/2021/13.txt").solve()
}

class Day13 {
    data class Problem(val points: Set<Point>, val folds: List<Fold>) {
        companion object {
            fun fromFile(fileName: String): Problem {
                val points = mutableSetOf<Point>()
                val folds = mutableListOf<Fold>()
                File(fileName).readLines().forEach { line ->
                    if (line.contains("=")) {
                        folds.add(Fold.fromString(line))
                    } else if (line.contains(",")) {
                        points.add(Point.fromString(line))
                    }
                }
                return Problem(points.toSet(), folds.toList())
            }
        }

        fun solve() {
            var currentPoints = points
            folds.forEach {
                currentPoints = it.foldPoints(currentPoints)
                println("Current size = ${currentPoints.size}")
            }

            val maxX = currentPoints.maxOf { it.x }
            val maxY = currentPoints.maxOf { it.y }

            println("Aww snap here comes the plot")
            for (y in 0..maxY) {
                for (x in 0..maxX) {
                    if (currentPoints.contains(Point(x, y))) {
                        print("O")
                    } else {
                        print(" ")
                    }
                }
                println()
            }
        }

    }

    data class Point(val x: Int, val y: Int) {
        companion object {
            fun fromString(line: String): Point {
                val whatevers = line.split(",")
                return Point(Integer.parseInt(whatevers[0]), Integer.parseInt(whatevers[1]))
            }
        }

        fun fold(fold: Fold): Point {
            return if (fold.isX) {
                val distance = kotlin.math.abs(x - fold.value)
                Point(fold.value - distance, y)
            } else {
                val distance = kotlin.math.abs(y - fold.value)
                Point(x, fold.value - distance)
            }
        }
    }

    data class Fold(val isX: Boolean, val value: Int) {
        companion object {
            fun fromString(line: String): Fold {
                val split = line.split(" ")[2].split("=")
                return Fold(split[0] == "x", Integer.parseInt(split[1]))
            }
        }

        fun foldPoints(points: Set<Point>): Set<Point> {
            return points.map { it.fold(this) }.toSet()
        }
    }
}
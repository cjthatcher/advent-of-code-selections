import java.io.File
import HeightMap.Coordinates as Coordinates

val basinMap = mutableMapOf<Coordinates, Coordinates>()

fun main(args: Array<String>) {
    val heights = File("resources/2021/9.txt").readLines()
        .map { it.toCharArray().map(::toHeight) }
    val heightMap = HeightMap(heights)

    heights.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, _ ->
            val myCoordinates = Coordinates(rowIndex, columnIndex)
            basinMap[myCoordinates] = getBasinFor(myCoordinates, heightMap)
        }
    }

    println("basinMap = $basinMap")
    val entriesByBasin =
        basinMap.entries.filterNot { entry -> entry.value == Coordinates(-1, -1) }.groupBy { entry -> entry.value }
    println("entries by basin = $entriesByBasin")
    val basinCount = entriesByBasin.mapValues { entry -> entry.value.size }
    println("basin and counts = $basinCount")
    val productOfLargestThree =
        basinCount.map { entry -> entry.value }.sorted().takeLast(3).fold(1) { acc: Int, value: Int -> acc * value }
    println("Product of largest three: $productOfLargestThree")
}

fun getBasinFor(coordinates: Coordinates, heightMap: HeightMap): Coordinates {
    val myCell = heightMap.getCell(coordinates)
    if (myCell.height == 9) {
        basinMap[coordinates] = Coordinates(-1, -1)
    }

    if (basinMap.containsKey(coordinates)) {
        return basinMap[coordinates]!!
    }

    if (myCell.height > heightMap.getCell(coordinates.rightOf()).height) {
        basinMap[coordinates] = getBasinFor(coordinates.rightOf(), heightMap)
    } else if (myCell.height > heightMap.getCell(coordinates.downOf()).height) {
        basinMap[coordinates] = getBasinFor(coordinates.downOf(), heightMap)
    } else if (myCell.height > heightMap.getCell(coordinates.leftOf()).height) {
        basinMap[coordinates] = getBasinFor(coordinates.leftOf(), heightMap)
    } else if (myCell.height > heightMap.getCell(coordinates.upOf()).height) {
        basinMap[coordinates] = getBasinFor(coordinates.upOf(), heightMap)
    } else {
        basinMap[coordinates] = coordinates
    }
    return basinMap[coordinates]!!
}

class HeightMap(val heights: List<List<Int>>) {
    private val rowCount = heights.size
    private val columnCount = heights[0].size

    fun getCell(coordinates: Coordinates): Cell = Cell(coordinates, getHeight(coordinates))

    private fun getHeight(coordinates: Coordinates): Int {
        return if (isOutOfBounds(coordinates)) {
            10
        } else {
            heights[coordinates.row][coordinates.column]
        }
    }

    private fun isOutOfBounds(coordinates: Coordinates) =
        coordinates.row < 0 || coordinates.row >= rowCount || coordinates.column < 0 || coordinates.column >= columnCount

    data class Coordinates(val row: Int, val column: Int) {
        fun rightOf(): Coordinates = Coordinates(row, column + 1)
        fun leftOf(): Coordinates = Coordinates(row, column - 1)
        fun upOf(): Coordinates = Coordinates(row - 1, column)
        fun downOf(): Coordinates = Coordinates(row + 1, column)
    }
}

data class Cell(val coordinates: Coordinates, val height: Int)

fun toHeight(char: Char): Int =
    when (char) {
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
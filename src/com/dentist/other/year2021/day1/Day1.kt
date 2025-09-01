import java.io.File

fun main(args: Array<String>) {
    val depthsFromFile = File("resources/2021/1.txt").readLines().map { Integer.parseInt(it) }
    val depthCount = depthsFromFile.size
    val threeDepths =
        depthsFromFile.mapIndexedNotNull(transform = { index: Int, currentValue: Int -> if (index + 2 >= depthCount) null else currentValue + depthsFromFile[index + 1] + depthsFromFile[index + 2] })
    val totalDecreasesInDepth =
        decreaseInDepth(threeDepths)


    println("count = $totalDecreasesInDepth")
}

private fun decreaseInDepth(depths: List<Int>): Int =
    depths
        .fold(initial = FoldResult(lastDepth = Int.MAX_VALUE),
            operation = { accumulatedResult: FoldResult, currentDepth: Int ->
                val delta = if (accumulatedResult.lastDepth < currentDepth) 1 else 0
                FoldResult(
                    count = accumulatedResult.count + delta,
                    lastDepth = currentDepth
                )
            }).count


data class FoldResult(val count: Int = 0, val lastDepth: Int)
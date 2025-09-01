import java.io.File

fun main(args: Array<String>) {
    val input = File("resources/2021/4.txt").readLines()
    val numbersToRead = input.first().split(",").map { Integer.parseInt(it) }

    val bingoCollector = hashMapOf<Int, List<Int>>()
    input.drop(1).filterNot(String::isEmpty).forEachIndexed { index: Int, it: String ->
        collectToBingoCard(index, bingoCollector, it)
    }
    val bingoCards = bingoCollector.mapValues { it -> BingoCard.fromList(it.value) }.values

//    val winningPair = readNumbers(numbersToRead, bingoCards)
    val worstCardPair = readLosingNumbers(numbersToRead, bingoCards)
    val product = worstCardPair?.second?.let { worstCardPair?.first?.sumOfUnmarkedNumbers()?.times(it) }

    println("Product = $product and losing card is ${worstCardPair?.first} and last number was ${worstCardPair?.second}")
}

private fun readNumbers(
    numbersToRead: List<Int>,
    bingoCards: Collection<BingoCard>
): Pair<BingoCard, Int>? {
    for (number in numbersToRead) {
        for (bingoCard in bingoCards) {
            bingoCard.markNumber(number)
            if (bingoCard.hasWon()) return Pair(bingoCard, number)
        }
    }
    return null
}

private fun readLosingNumbers(
    numbersToRead: List<Int>,
    bingoCards: Collection<BingoCard>
): Pair<BingoCard, Int>? {
    val remainingIds = bingoCards.map { it -> it.id }.toMutableSet()
    for (number in numbersToRead) {
        for (bingoCard in bingoCards) {
            bingoCard.markNumber(number)
            if (bingoCard.hasWon()) {
                remainingIds.remove(bingoCard.id)
                if (remainingIds.isEmpty()) {
                    return Pair(bingoCard, number)
                }
            }
        }
    }
    return null
}

private fun collectToBingoCard(
    index: Int,
    bingoCollector: HashMap<Int, List<Int>>,
    line: String
) {
    val bingoCardIndex = index / 5
    bingoCollector[bingoCardIndex] = bingoCollector.getOrDefault(bingoCardIndex, listOf<Int>())
        .plus(line.trim().split(" ").filterNot(String::isEmpty).map { Integer.parseInt(it) })
}

class BingoCard(
    private val numberToIndexMap: Map<Int, Int>,
    private val markedIndices: MutableMap<Int, Boolean>,
    private val numbers: List<Int>,
    val id: String
) {

    companion object {
        fun fromList(numbers: List<Int>): BingoCard {
            val bingoNumberToPositionInList =
                numbers.mapIndexed { index: Int, number: Int -> Pair(number, index) }.associate { it }
            return BingoCard(bingoNumberToPositionInList, mutableMapOf(), numbers, numbers.hashCode().toString())
        }
    }

    fun markNumber(numberToMark: Int) = numberToIndexMap[numberToMark]?.also { it: Int -> markedIndices[it] = true }
    fun hasWon(): Boolean {
        if (allMarked(0, 1, 2, 3, 4)) return true
        if (allMarked(5, 6, 7, 8, 9)) return true
        if (allMarked(10, 11, 12, 13, 14)) return true
        if (allMarked(15, 16, 17, 18, 19)) return true
        if (allMarked(20, 21, 22, 23, 24)) return true

        if (allMarked(0, 5, 10, 15, 20)) return true
        if (allMarked(1, 6, 11, 16, 21)) return true
        if (allMarked(2, 7, 12, 17, 22)) return true
        if (allMarked(3, 8, 13, 18, 23)) return true
        if (allMarked(4, 9, 14, 19, 24)) return true

//        if (allMarked(0, 6, 12, 18, 24)) return true
//        if (allMarked(4, 8, 12, 16, 20)) return true

        return false
    }

    private fun allMarked(a: Int, b: Int, c: Int, d: Int, e: Int): Boolean {
        return called(a).and(called(b)).and(called(c)).and(called(d)).and(called(e))
    }

    private fun called(index: Int): Boolean {
        return markedIndices.getOrDefault(index, defaultValue = false)
    }

    fun sumOfUnmarkedNumbers(): Int {
        return numbers.filter { it ->
            val index = numberToIndexMap.getOrDefault(it, -1)
            markedIndices.getOrDefault(index, false).equals(false)
        }.reduce({ a, b -> a + b })
    }

    override fun toString(): String {
        return "BingoCard(numberToIndexMap=$numberToIndexMap, markedIndices=$markedIndices, numbers=$numbers, id='$id')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BingoCard

        if (numberToIndexMap != other.numberToIndexMap) return false
        if (markedIndices != other.markedIndices) return false
        if (numbers != other.numbers) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = numberToIndexMap.hashCode()
        result = 31 * result + markedIndices.hashCode()
        result = 31 * result + numbers.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }

}
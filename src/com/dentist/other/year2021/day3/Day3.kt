import java.io.File
import java.util.*
import kotlin.text.StringBuilder

fun main(args: Array<String>) {
    val bunchOfBytes = File("resources/2021/3.txt").readLines().map(Bytes.Companion::fromString)
    day3Simple(bunchOfBytes)
    day3Complex(bunchOfBytes)
}

private fun day3Complex(bunchOfBytes: List<Bytes>) {
    val oxygenValue = findSurvivor(bunchOfBytes, mostCommonBitMapper).asDecimalInt()
    val co2Value = findSurvivor(bunchOfBytes, leastCommonBitMapper).asDecimalInt()

    println("o2 = $oxygenValue; co2 = $co2Value; product = ${oxygenValue * co2Value}")
}

val mostCommonBitMapper: (Int) -> (Int) -> Int =
    { numberOfSamples: Int -> mostCommonBit(numberOfSamples) }

private fun mostCommonBit(numberOfSamples: Int): (Int) -> Int =
    { if (it >= numberOfSamples / 2.0) 1 else 0 }

val leastCommonBitMapper: (Int) -> (Int) -> Int =
    { numberOfSamples: Int -> leastCommonBit(numberOfSamples) }

private fun leastCommonBit(numberOfSamples: Int): (Int) -> Int =
    { if (it >= numberOfSamples / 2.0) 0 else 1 }

private fun findSurvivor(bunchOfBytes: List<Bytes>, bitMapper: (Int) -> (Int) -> Int): Bytes {
    var filteredBytes = bunchOfBytes
    var index = 0
    while (filteredBytes.size > 1) {
        filteredBytes = filterBytesOnIndex(
            index++,
            filteredBytes,
            bitMapper
        )
    }
    return filteredBytes.first()
}

private fun filterBytesOnIndex(
    index: Int,
    bunchOfBytes: List<Bytes>,
    bitMapper: (Int) -> (Int) -> Int
): List<Bytes> {
    val summedBytes = bunchOfBytes.reduce(Bytes::add)
    val mostCommonBits = Bytes(summedBytes.bytes.map(bitMapper(bunchOfBytes.size)))
    return bunchOfBytes.filter { it.bytes[index] == mostCommonBits.bytes[index] }
}

private fun day3Simple(bunchOfBytes: List<Bytes>) {
    val summedBytes = bunchOfBytes.reduce(Bytes::add)
    val gammaRateBytes = Bytes(summedBytes.bytes.map(mostCommonBit(bunchOfBytes.size)))
    val epsilonRateBytes = Bytes(summedBytes.bytes.map(leastCommonBit(bunchOfBytes.size)))

    val decimalGamma = gammaRateBytes.asDecimalInt()
    val decimalEpsilon = epsilonRateBytes.asDecimalInt()

    println("decimalGamma = $decimalGamma; decimalEpsilon = $decimalEpsilon; power rate = ${decimalGamma * decimalEpsilon}")
}

data class Bytes(val bytes: List<Int>) {
    companion object {
        fun fromString(s: String): Bytes {
            val bytes = ArrayList<Int>(s.length);
            for (char in s.toCharArray()) {
                if (char == '1') {
                    bytes.add(1);
                } else {
                    bytes.add(0);
                }
            }
            return Bytes(bytes)
        }
    }

    fun add(other: Bytes): Bytes {
        return Bytes(bytes.mapIndexed { index, it -> it + other.bytes[index] })
    }

    fun asString(): String {
        val sb = StringBuilder()
        bytes.forEach { sb.append(it) }
        return sb.toString();
    }

    fun asDecimalInt(): Int = Integer.parseInt(asString(), 2);
}
import Segment.*
import java.io.File

fun main(args: Array<String>) {
    val lines = File("resources/2021/8.txt").readLines().map { Line.fromString(it) }
    var sum = 0
    for (line in lines) {
        sum += line.solve()
    }
    println("easy sum $sum")
}

data class Line(val numbers: List<Digit>, val output: List<Digit>) {
    fun solve(): Int {
        //Goal: Determine a mapping from Digit to (0-9), then output the four digit code.

        var knownSegmentMap = mutableMapOf<Segment, Segment>()
        var knownDigitMap = mutableMapOf<Digit, KnownDigit>()
        var possibleSegmentMappings = makeFullPossibilitiesMap()

        for (unidentifiedSegment in possibleSegmentMappings.keys) {
            var count = 0
            for (unknownDigit in numbers) {
                if (unknownDigit.segments.contains(unidentifiedSegment)) {
                    count++
                }
            }
            if (count == 6) { // B uniquely appears in 6 displays
                possibleSegmentMappings.put(unidentifiedSegment, setOf(B))
                knownSegmentMap.put(unidentifiedSegment, B)
            }

            if (count == 4) { // E unique appears in 4 displays
                possibleSegmentMappings.put(unidentifiedSegment, setOf(E))
                knownSegmentMap.put(unidentifiedSegment, E)
            }
        }


        while (knownDigitMap.size < 10) {
            val digitPossibilities = makePossibleDigitsMap(knownSegmentMap, knownDigitMap, possibleSegmentMappings)

            for (entry in digitPossibilities.entries) {
                if (entry.value.size == 1) {
                    knownDigitMap.put(entry.key, entry.value.first())
                }
            }

            for (entry in knownDigitMap.entries) {
                for (segment in entry.key.segments) {
                    possibleSegmentMappings[segment] =
                        possibleSegmentMappings.getOrDefault(segment, setOf()).intersect(entry.value.digit.segments)
                }
            }

            for (identifiedDigit in knownDigitMap.keys) {
                for (segment in identifiedDigit.segments) {
                    val possibleMappings = possibleSegmentMappings[segment]
                    var uniquePossibilities = mutableSetOf<Pair<Segment, Segment>>()
                    if (possibleMappings != null) {
                        for (possibleMapping in possibleMappings) {
                            var uniqueMapping = true
                            for (otherSegment in identifiedDigit.segments) {
                                if (otherSegment.equals(segment)) {
                                    continue
                                }
                                val theirMappings = possibleSegmentMappings[otherSegment]
                                if (theirMappings != null && theirMappings.contains(possibleMapping)) {
                                    uniqueMapping = false
                                }
                            }
                            if (uniqueMapping == true) {
                                uniquePossibilities.add(Pair(segment, possibleMapping))
                            }
                        }
                    }
                    if (uniquePossibilities.size == 1) {
                        val chickenDinner = uniquePossibilities.first()
                        knownSegmentMap.put(chickenDinner.first, chickenDinner.second)
                        possibleSegmentMappings.put(chickenDinner.first, setOf(chickenDinner.second))
                    }
                }
            }
        }
        println("${output(knownDigitMap)}")
        return output(knownDigitMap)
    }

    private fun output(knownDigitMap: Map<Digit, KnownDigit>): Int =
        knownDigitMap.get(output[0])?.intValue!! * 1000 + knownDigitMap.get(output[1])
            ?.intValue!! * 100 + knownDigitMap.get(output[2])?.intValue!! * 10 + knownDigitMap.get(output[3])
            ?.intValue!!


    private fun makePossibleDigitsMap(
        knownSegmentMap: MutableMap<Segment, Segment>,
        knownDigitMap: MutableMap<Digit, KnownDigit>,
        possibleSegmentMappings: MutableMap<Segment, Set<Segment>>
    ): Map<Digit, Set<KnownDigit>> {
        val possibleDigitsMap = mutableMapOf<Digit, Set<KnownDigit>>()
        for (digit in numbers) {
            if (knownDigitMap.containsKey(digit)) {
                possibleDigitsMap[digit] = setOf(knownDigitMap.getOrDefault(digit, KnownDigit.ZERO))
                continue;
            }
            possibleDigitsMap[digit] = digit.getPossibleValues(possibleSegmentMappings)
        }
        return possibleDigitsMap
    }

    private fun makeFullPossibilitiesMap(): MutableMap<Segment, Set<Segment>> {
        return mutableMapOf(
            A to setOf(A, B, C, D, E, F, G),
            B to setOf(A, B, C, D, E, F, G),
            C to setOf(A, B, C, D, E, F, G),
            D to setOf(A, B, C, D, E, F, G),
            E to setOf(A, B, C, D, E, F, G),
            F to setOf(A, B, C, D, E, F, G),
            G to setOf(A, B, C, D, E, F, G)
        )
    }

    fun getEasyOutputCount(): Int {
        return output.filter {
            it.segments.size == 2 || it.segments.size == 3 || it.segments.size == 4 || it.segments.size == 7
        }.count()
    }

    companion object {
        fun fromString(input: String): Line {
            val parts = input.split("|");
            val digits =
                parts[0].trim().split(" ").map { it.toCharArray().map { char -> Segment.fromChar(char) } }
                    .map { Digit(it.toMutableSet()) }
            val output =
                parts[1].trim().split(" ").map { it.toCharArray().map { char -> Segment.fromChar(char) } }
                    .map { Digit(it.toMutableSet()) }
            return Line(digits, output)
        }
    }
}

data class Digit(val segments: Set<Segment>) {
    fun getPossibleValues(possibleSegmentMappings: MutableMap<Segment, Set<Segment>>): Set<KnownDigit> {
        val possibilities = mutableSetOf<KnownDigit>()
        for (knownDigit in KnownDigit.values()) {
            if (segments.size != knownDigit.digit.segments.size) {
                continue
            } else {
                var stillContainsMySegments = true
                for (char in segments) {
                    if (!knownDigit.containsAtLeastOneOf(possibleSegmentMappings[char])) {
                        stillContainsMySegments = false
                        break
                    }
                }
                if (stillContainsMySegments) {
                    possibilities.add(knownDigit)
                }
            }
        }
        return possibilities
    }
}

enum class KnownDigit(val digit: Digit, val intValue: Int) {
    ZERO(Digit(setOf(A, B, C, E, F, G)), 0),
    ONE(Digit(setOf(C, F)), 1),
    TWO(Digit(setOf(A, C, D, E, G)), 2),
    THREE(Digit(setOf(A, C, D, F, G)), 3),
    FOUR(Digit(setOf(B, C, D, F)), 4),
    FIVE(Digit(setOf(A, B, D, F, G)), 5),
    SIX(Digit(setOf(A, B, D, E, F, G)), 6),
    SEVEN(Digit(setOf(A, C, F)), 7),
    EIGHT(Digit(setOf(A, B, C, D, E, F, G)), 8),
    NINE(Digit(setOf(A, B, C, D, F, G)), 9);

    fun containsSegment(segment: Segment?): Boolean {
        if (segment == null) return false
        return digit.segments.contains(segment)
    }

    fun containsAtLeastOneOf(segments: Set<Segment>?): Boolean {
        if (segments == null) {
            return false
        }
        if (segments.isEmpty()) {
            return false;
        }
        for (segment in segments) {
            if (digit.segments.contains(segment)) {
                return true
            }
        }
        return false
    }

}

enum class Segment {
    A, B, C, D, E, F, G;

    companion object {
        fun fromChar(character: Char): Segment {
            if (character == 'a') return A
            if (character == 'b') return B
            if (character == 'c') return C
            if (character == 'd') return D
            if (character == 'e') return E
            if (character == 'f') return F
            return G
        }
    }
}
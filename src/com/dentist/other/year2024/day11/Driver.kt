package com.dentist.other.year2024.day11

import java.io.File

fun main(args:Array<String>) {
    val lines = File("resources/2024/11.txt").readLines()
    val input = lines[0]
    val stones = input.split(" ").map { it -> Stone(it.toLong()) }.toList()
    var gen = Generation(stones.associate { it to 1 })
    for (i in 1..75) {
        gen = gen.evolve()
        println("After $i generations there are ${gen.size()} stones")
    }
}

data class Generation(val times: Map<Stone, Long>) {
    fun size() = times.values.sum()
    fun evolve(): Generation {
        val res = mutableMapOf<Stone, Long>()

        for(e in times.entries) {
            for (stone in e.key.evolve()) {
                res.putIfAbsent(stone, 0)
                res.compute(stone) { key, existing -> existing?.plus(e.value) }
            }
        }

        return Generation(res.toMap())
    }
}

data class Stone(val engraving:Long) {
    fun evolve(): List<Stone> {
        //If the stone is engraved with the number 0, it is replaced by a stone engraved with the number 1.
        if (engraving == 0L) {
            return listOf(Stone(1))
        }
        //If the stone is engraved with a number that has an even number of digits, it is replaced by two stones. The left half of the digits are engraved on the new left stone, and the right half of the digits are engraved on the new right stone. (The new numbers don't keep extra leading zeroes: 1000 would become stones 10 and 0.)
        if (evenDigits()) {
            return listOf(leftStone(), rightStone())
        }
        //If none of the other rules apply, the stone is replaced by a new stone; the old stone's number multiplied by 2024 is engraved on the new stone.
        return listOf(Stone(engraving*2024))
    }

    private fun evenDigits() = engraving.toString().length % 2 == 0
    private fun leftStone():Stone {
        try {
            val leftNum = engraving.toString()
                .substring(0, engraving.toString().length / 2) // substring is start inclusive and end exclusive iirc...
            return Stone(leftNum.toLong())
        } catch (e:Exception) {
            println("wtf the engraving was $engraving")
            throw e
        }
    }

    private fun rightStone():Stone {
        val rightNum = engraving.toString().substring(engraving.toString().length / 2) // substring is start inclusive and end exclusive iirc...
        return Stone(rightNum.toLong())
    }
}
package com.dentist.other.year2024.day7

import org.apache.kafka.common.protocol.types.Field
import java.io.File
import kotlin.math.pow

fun main(args: Array<String>) {
    val lines = File("resources/2024/7.txt").readLines()
    val sum = lines.map { Problem.fromString(it) }.filter { it.viableComplex() }.sumOf { it.target }
    println("The final sum is $sum")
}

private data class Problem(val target: Long, val operands: List<Long>) {

    companion object {
        fun fromString(line: String): Problem {
            val parts = line.split(": ")
            val target = parts[0].toLong()
            val operands = parts[1].split(" ").map { it.toLong() }.toList()
            return Problem(target, operands)
        }
    }

    fun viableSimple(): Boolean {
        // There are two possible operators between each of the operands.
        // We can count up by one in binary, and use that to tell us which operators are mult(1) vs add(0).
        val operators = operands.size - 1
        val max = 1 shl operators // Should be 1 * 2^operators.
        for (i in 0..max) {
            var res = operands.first()
            var operatorBits = i
            for (o in operands.subList(1, operands.size)) {
                res = if (operatorBits % 2 == 1) res * o else res + o
                operatorBits = operatorBits shr 1
            }
//            println("For target $target from $operands , with i == $i we got $res")
            if (res == target) {
                return true;
            }
        }
        return false;
    }

    fun viableComplex(): Boolean {
        // There are two possible operators between each of the operands.
        // We can count up by one in binary, and use that to tell us which operators are mult(1) vs add(0).
        val operators = operands.size - 1
        val max = 3f.pow(operators).toInt()
        for (i in 0..max) {
            var res = operands.first()
            var operatorBits = i
            for (o in operands.subList(1, operands.size)) {
                val operator = operatorBits % 3
                res =
                    if (operator == 2) {
                        res * o
                    } else if (operator == 1) {
                        res + o
                    } else {
                        "$res$o".toLong()
                    }
                operatorBits = operatorBits / 3
            }
//            println("For target $target from $operands , with i == $i we got $res")
            if (res == target) {
                return true;
            }
        }
        return false;
    }
}
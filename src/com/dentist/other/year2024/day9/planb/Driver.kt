package com.dentist.other.year2024.day9.planb

import java.io.File
import java.util.Deque
import java.util.LinkedList

fun main(args:Array<String>) {
    // we get an input String, which represents the disk.
    // We're going to iterate through it, until we meet in the middle.
    // We have a running long that is the sum of file number * index.
    // So we need to figure out the file number of everyone first.
    // I don't _really_ want to store the data. I could, I guess, but I'd prefer not to.

    val lines = File("resources/2024/9.txt").readLines()
    val input = lines[0]
    println(SecondDefrag.consume(input))
}

class SecondDefrag {
    companion object {
        fun consume(input:String):Long {
            var totalDiskBlocks = 0
            val leftDeque: LinkedList<Int> = LinkedList<Int>()
            val rightDeque: LinkedList<Int> = LinkedList<Int>()

            var fileId = 0
            var isFile = true

            for (char in input.toCharArray()) {
                val numberOfBlocks = char.toString().toInt()

                if (isFile) {
                    for (counter in 1..numberOfBlocks) {
                        leftDeque.add(fileId)
                        rightDeque.push(fileId)
                    }
                    // put fileId numberOfBlocks times on the back of left queue.
                    // put fileid numberOfBlocks times on the front of right queue.
                    fileId++
                    totalDiskBlocks += numberOfBlocks
                }

                isFile = !isFile
            }

            var checkSum = 0L
            var index = 0
            var isLeft = true

            for (char in input.toCharArray()) {
                val numberOfBlocks = char.toString().toInt()
                for (counter in 1..numberOfBlocks) {
                    val fileId = if (isLeft) leftDeque.pop() else rightDeque.pop()
                    checkSum += (index * fileId)
                    index++

                    if (index == totalDiskBlocks) {
                        return checkSum
                    }
                }
                isLeft = !isLeft
            }
            return checkSum
        }
    }
}

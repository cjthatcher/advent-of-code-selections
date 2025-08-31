import java.io.File

fun main(args: Array<String>) {
    val ages = File("resources/2021/6.txt").readLines().first().split(",").map { Integer.parseInt(it) }
    val ageArray = longArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    for (age in ages) {
        ageArray[age]++
    }

    var freshAgeArray = ageArray
    for (i in 1..256) {
        freshAgeArray = makeBabies(freshAgeArray)
        println("After $i days the value is ${freshAgeArray.sum()}")
    }
}

fun makeBabies(ageArray: LongArray): LongArray =
    longArrayOf(
        ageArray[1], // 0
        ageArray[2], // 1
        ageArray[3], // 2
        ageArray[4], // 3
        ageArray[5], // 4
        ageArray[6], // 5
        ageArray[7] + ageArray[0], // 6. Old 7's and Old 0's
        ageArray[8], // old 8s
        ageArray[0] // new babies
    )
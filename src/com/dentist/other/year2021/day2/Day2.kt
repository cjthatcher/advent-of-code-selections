import java.io.File

fun main(args: Array<String>) {
    val instructions = File("resources/2021/2.txt").readLines().map(Instruction.Companion::fromString)
    day2Simple(instructions)
    day2Complex(instructions)
}

private fun day2Simple(instructions: List<Instruction>) {
    val endCoordinates =
        instructions.fold(
            initial = SimpleCoordinates(0, 0),
            operation = SimpleCoordinates::plus
        )

    println("Simple output: depth: ${endCoordinates.depth}; horizontal: ${endCoordinates.horizontal}; product: ${endCoordinates.depth * endCoordinates.horizontal}")
}

private fun day2Complex(instructions: List<Instruction>) {
    val endCoordinates = instructions.fold(initial = Coordinates(0, 0, 0), operation = Coordinates::plus)

    println("Complex output: depth: ${endCoordinates.depth}; horizontal: ${endCoordinates.horizontal}; aim: ${endCoordinates.aim}; product: ${endCoordinates.depth * endCoordinates.horizontal}")
}


data class SimpleCoordinates(val horizontal: Int, val depth: Int) {
    fun plus(instruction: Instruction): SimpleCoordinates {
        return SimpleCoordinates(
            horizontal = horizontal + instruction.horizontalDelta(),
            depth = depth + instruction.depthDelta()
        )
    }
}

data class Coordinates(val horizontal: Int, val depth: Int, val aim: Int) {
    fun plus(instruction: Instruction): Coordinates {
        val freshAim = aim + instruction.depthDelta()
        return Coordinates(
            horizontal = horizontal + instruction.horizontalDelta(),
            depth = depth + (freshAim * instruction.horizontalDelta()),
            aim = freshAim
        )
    }
}

data class Instruction(val direction: Direction, val magnitude: Int) {
    companion object {
        fun fromString(s: String): Instruction {
            val parts = s.split(" ")
            return Instruction(Direction.fromString(parts[0]), Integer.parseInt(parts[1]))
        }
    }

    fun horizontalDelta(): Int = if (direction == Direction.FORWARD) magnitude else 0

    fun depthDelta(): Int =
        if (direction == Direction.DOWN) magnitude else if (direction == Direction.UP) -magnitude else 0
}

enum class Direction {
    FORWARD, DOWN, UP, UNKNOWN;

    companion object {
        fun fromString(s: String): Direction =
            if (s.equals("forward")) FORWARD
            else if (s.equals("down")) DOWN
            else if (s.equals("up")) UP
            else UNKNOWN
    }

}
fun main() {
    fun String.toHash(): Int {
        return this.fold(0) { acc, c ->
            ((acc + c.code) * 17) % 256
        }
    }

    fun part1(input: List<String>): Int {
        return input.first().split(",").sumOf {
            it.toHash()
        }
    }

    fun part2(input: List<String>): Int {
        val boxes = List(256) { mutableMapOf<String, Int>() }

        input.first().split(",").forEach { lensInstruction ->
            val (label, focalLength) = lensInstruction.split("=", "-")
            val boxIndex = label.toHash()

            if ('=' in lensInstruction) {
                boxes[boxIndex][label] = focalLength.toInt()
            } else {
                boxes[boxIndex].remove(label)
            }
        }

        return boxes.mapIndexed { boxIndex, box ->
            box.toList().mapIndexed { index, (_, focalLength) ->
                (boxIndex + 1) * (index + 1) * focalLength
            }.sum()
        }.sum()
    }

    val testInput = readInput("Day15Test")
    val input = readInput("Day15")

    println("Advent of Code 2023 - Day 15")
    println("----------------------------")

    check(part1(testInput) == 1320)
    println("Solution for part1: ${part1(input)}")

    check(part2(testInput) == 145)
    println("Solution for part2: ${part2(input)}")
}

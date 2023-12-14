fun main() {
    fun List<String>.transpose() = this.map { it.toList() }.transpose().map { it.joinToString("") }

    fun List<String>.tiltWest(): List<String> {
        return this.map { line ->
            line.split("#").joinToString("#") { part ->
                val numberOfStones = part.count { it == 'O' }
                "O".repeat(numberOfStones) + ".".repeat(part.length - numberOfStones)
            }
        }
    }

    fun List<String>.tiltEast() = this.map { it.reversed() }.tiltWest().map { it.reversed() }

    fun List<String>.tiltNorth() = this.transpose().tiltWest().transpose()

    fun List<String>.tiltSouth() = this.transpose().map { it.reversed() }.tiltWest().map { it.reversed() }.transpose()

    fun List<String>.calculateLoad() = this.reversed().mapIndexed { index, line ->
        line.count { it == 'O' } * (index + 1)
    }.sum()

    fun part1(input: List<String>): Int {
        val inputAfterTiltNorth = input.tiltNorth()

        return inputAfterTiltNorth.calculateLoad()
    }

    fun part2(input: List<String>): Int {
        var pattern = input

        val seenPatterns = mutableListOf(pattern)
        var moduloOffset = 0
        var modulo = Long.MAX_VALUE

        for (i in 1..1_000_000_000L) {
            pattern = pattern.tiltNorth().tiltWest().tiltSouth().tiltEast()
            if (pattern in seenPatterns) {
                moduloOffset = seenPatterns.indexOf(pattern)
                modulo = i - moduloOffset
                break
            }
            seenPatterns += pattern
        }

        val solutionIndex = ((1_000_000_000L - moduloOffset) % modulo) + moduloOffset
        val solution = seenPatterns[solutionIndex.toInt()]

        return solution.calculateLoad()
    }

    val testInput = readInput("Day14Test")
    val input = readInput("Day14")

    println("Advent of Code 2023 - Day 14")
    println("----------------------------")

    check(part1(testInput) == 136)
    println("Solution for part1: ${part1(input)}")

    check(part2(testInput) == 64)
    println("Solution for part2: ${part2(input)}")
}

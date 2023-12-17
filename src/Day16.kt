import kotlin.math.max

fun main() {
    operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
        return this.first + other.first to this.second + other.second
    }

    val up = 0 to -1
    val down = 0 to 1
    val left = -1 to 0
    val right = 1 to 0

    fun nextDirections(currentTile: Char, lastDirection: Pair<Int, Int>): List<Pair<Int, Int>> {
        if (currentTile == '.') return listOf(lastDirection)

        return when (currentTile to lastDirection) {
            '/' to up -> listOf(right)
            '/' to down -> listOf(left)
            '/' to left -> listOf(down)
            '/' to right -> listOf(up)

            '\\' to up -> listOf(left)
            '\\' to down -> listOf(right)
            '\\' to left -> listOf(up)
            '\\' to right -> listOf(down)

            '|' to up, '|' to down -> listOf(lastDirection)
            '|' to left, '|' to right -> listOf(up, down)

            '-' to left, '-' to right -> listOf(lastDirection)
            '-' to up, '-' to down -> listOf(left, right)
            else -> error("Unknown tile $currentTile and direction $lastDirection combo.")
        }
    }

    fun traverseGrid(
        grid: List<List<Char>>,
        startPosition: Pair<Int, Int>,
        startDirection: Pair<Int, Int>
    ): Set<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
        val xRange = 0..<grid.first().size
        val yRange = grid.indices

        var currentSteps = mutableListOf(startPosition to startDirection)
        val stepsSeen = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()

        while (currentSteps.isNotEmpty()) {
            stepsSeen.addAll(currentSteps)

            val nextSteps = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
            for ((tilePosition, lastDirection) in currentSteps) {
                val nextDirections = nextDirections(grid[tilePosition.second][tilePosition.first], lastDirection)

                for (nextDirection in nextDirections) {
                    val nextPosition = tilePosition + nextDirection

                    if (nextPosition.first in xRange && nextPosition.second in yRange && nextPosition to nextDirection !in stepsSeen)
                        nextSteps.add(nextPosition to nextDirection)
                }
            }
            currentSteps = nextSteps
            stepsSeen.addAll(currentSteps)
        }
        return stepsSeen
    }

    fun part1(input: List<String>): Int {
        val grid = input.map { it.toList() }

        val stepsSeen = traverseGrid(grid, 0 to 0, right)
        return stepsSeen.map { it.first }.toSet().size
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.toList() }

        var maxEnergized = 0

        for (i in grid.indices) {
            maxEnergized = max(maxEnergized, traverseGrid(grid, 0 to i, right).map { it.first }.toSet().size)
            maxEnergized =
                max(maxEnergized, traverseGrid(grid, grid.first().size - 1 to i, left).map { it.first }.toSet().size)
        }

        for (i in grid.first().indices) {
            maxEnergized = max(maxEnergized, traverseGrid(grid, i to 0, down).map { it.first }.toSet().size)
            maxEnergized = max(maxEnergized, traverseGrid(grid, i to grid.size - 1, up).map { it.first }.toSet().size)
        }

        return maxEnergized
    }

    val testInput = readInput("Day16Test")
    val input = readInput("Day16")

    println("Advent of Code 2023 - Day 16")
    println("----------------------------")

    check(part1(testInput) == 46)
    println("Solution for part1: ${part1(input)}")

    check(part2(testInput) == 51)
    println("Solution for part2: ${part2(input)}")
}

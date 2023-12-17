package day16

import readInput
import kotlin.math.max

typealias Position = Pair<Int, Int>
typealias Direction = Pair<Int, Int>

fun main() {
    operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
        return this.first + other.first to this.second + other.second
    }

    val up: Direction = 0 to -1
    val down: Direction = 0 to 1
    val left: Direction = -1 to 0
    val right: Direction = 1 to 0

    fun nextDirections(currentTile: Char, lastDirection: Direction): List<Direction> {
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

    fun List<List<Char>>.traverseBeam(
        startPosition: Position,
        startDirection: Direction
    ): Set<Pair<Position, Direction>> {
        val xRange = 0..<this.first().size
        val yRange = this.indices

        var currentSteps = mutableListOf(startPosition to startDirection)
        val stepsSeen = mutableSetOf<Pair<Position, Direction>>()

        while (currentSteps.isNotEmpty()) {
            stepsSeen.addAll(currentSteps)

            val nextSteps = mutableListOf<Pair<Position, Direction>>()
            for ((tilePosition, lastDirection) in currentSteps) {
                val nextDirections = nextDirections(this[tilePosition.second][tilePosition.first], lastDirection)

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

    fun Set<Pair<Position, Direction>>.calculateEnergizeValue() = this.map { it.first }.toSet().size

    fun part1(input: List<String>): Int {
        val grid = input.map { it.toList() }

        val stepsSeen = grid.traverseBeam(0 to 0, right)
        return stepsSeen.calculateEnergizeValue()
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.toList() }

        var maxEnergizedValue = 0

        for (i in grid.indices) {
            maxEnergizedValue = max(maxEnergizedValue, grid.traverseBeam(0 to i, right).calculateEnergizeValue())
            maxEnergizedValue =
                max(maxEnergizedValue, grid.traverseBeam(grid.first().size - 1 to i, left).calculateEnergizeValue())
        }

        for (i in grid.first().indices) {
            maxEnergizedValue = max(maxEnergizedValue, grid.traverseBeam(i to 0, down).calculateEnergizeValue())
            maxEnergizedValue =
                max(maxEnergizedValue, grid.traverseBeam(i to grid.size - 1, up).calculateEnergizeValue())
        }

        return maxEnergizedValue
    }

    val testInput = readInput("day16/TestInput")
    val input = readInput("day16/PuzzleInput")

    println("Advent of Code 2023 - Day 16")
    println("----------------------------")

    check(part1(testInput) == 46)
    println("Solution for part1: ${part1(input)}")

    check(part2(testInput) == 51)
    println("Solution for part2: ${part2(input)}")
}

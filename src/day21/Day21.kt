package day21

import readInput
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow

typealias Position = Pair<Int, Int>
typealias Direction = Pair<Int, Int>

val UP: Direction = 0 to -1
val RIGHT: Direction = 1 to 0
val DOWN: Direction = 0 to 1
val LEFT: Direction = -1 to 0

operator fun Position.plus(dir: Direction): Position {
    return this.first + dir.first to this.second + dir.second
}

operator fun <T> List<List<T>>.get(position: Position): T? {
    return this.getOrNull(position.second)?.getOrNull(position.first)
}

fun main() {
    fun List<String>.parseInput(): Pair<List<List<Char>>, Position> {
        val map = this.map { line ->
            line.toList().map { if (it == 'S') '.' else it }
        }
        val startPosition: Position = this.mapIndexed { row, line ->
            line.mapIndexed { col, c ->
                if (c == 'S') col to row
                else null
            }
        }.flatten().filterNotNull().first()
        return map to startPosition
    }

    fun reachableSpaces(map: List<List<Char>>, startPosition: Position, steps: Int): Int {
        var currentPositions = setOf(startPosition)
        for (i in 1..steps) {
            val nextPositions = mutableSetOf<Position>()
            for (position in currentPositions) {
                for (dir in listOf(UP, RIGHT, DOWN, LEFT)) {
                    val newPos = position + dir
                    val char = map[newPos]
                    if (char != null && char == '.') {
                        nextPositions += newPos
                    }
                }
            }
            currentPositions = nextPositions
        }
        return currentPositions.count()
    }

    fun part1(input: List<String>, steps: Int): Int {
        val (map, startPosition) = input.parseInput()

        return reachableSpaces(map, startPosition, steps)
    }

    // with big help from https://www.youtube.com/watch?v=9UOMZSL0JTg
    fun part2(input: List<String>, steps: Int): Long {
        val (map, startPosition) = input.parseInput()

        assert(map.size == map.first().size)
        val size = map.size

        assert(startPosition.first == size / 2 && startPosition.second == size / 2)
        assert(steps % size == size / 2)

        val gridWidth = steps / size - 1

        val numberOfOddGrids = (floor(gridWidth / 2.0) * 2 + 1).pow(2).toLong()
        val numberOfEvenGrids = (ceil(gridWidth.toDouble()) + 1).pow(2).toLong()

        val reachableSpacesInOddGris = reachableSpaces(map, startPosition, size * 2 + 1).toLong()
        val reachableSpacesInEvenGris = reachableSpaces(map, startPosition, size * 2).toLong()

        val reachableSpacesTopCorner = reachableSpaces(map,   startPosition.first to size - 1, size - 1).toLong()
        val reachableSpacesRightCorner = reachableSpaces(map, 0 to startPosition.second , size - 1).toLong()
        val reachableSpacesBottomCorner = reachableSpaces(map, startPosition.first to 0, size - 1).toLong()
        val reachableSpacesLeftCorner = reachableSpaces(map, size - 1 to startPosition.second, size - 1).toLong()

        val restStepsSmallTriangles = (floor((size / 2.0)) - 1).toInt()
        val reachableSpacesSmallTopRight = reachableSpaces(map, 0 to size - 1, restStepsSmallTriangles).toLong()
        val reachableSpacesSmallTopLeft = reachableSpaces(map, size - 1 to size - 1, restStepsSmallTriangles).toLong()
        val reachableSpacesSmallBottomRight = reachableSpaces(map, 0 to 0, restStepsSmallTriangles).toLong()
        val reachableSpacesSmallBottomLeft = reachableSpaces(map, size - 1 to 0, restStepsSmallTriangles).toLong()

        val restStepsLargeTriangles = (floor((size * 3.0 / 2.0)) - 1).toInt()
        val reachableSpacesLargeTopRight = reachableSpaces(map, 0 to size - 1 , restStepsLargeTriangles).toLong()
        val reachableSpacesLargeTopLeft = reachableSpaces(map, size - 1 to size - 1, restStepsLargeTriangles).toLong()
        val reachableSpacesLargeBottomRight = reachableSpaces(map, 0 to 0, restStepsLargeTriangles).toLong()
        val reachableSpacesLargeBottomLeft = reachableSpaces(map, size - 1 to 0, restStepsLargeTriangles).toLong()

        return reachableSpacesInOddGris * numberOfOddGrids +
            reachableSpacesInEvenGris * numberOfEvenGrids +
            reachableSpacesTopCorner + reachableSpacesRightCorner + reachableSpacesBottomCorner + reachableSpacesLeftCorner +
            (gridWidth + 1L) * (reachableSpacesSmallTopRight + reachableSpacesSmallTopLeft + reachableSpacesSmallBottomRight + reachableSpacesSmallBottomLeft) +
            gridWidth * (reachableSpacesLargeTopRight + reachableSpacesLargeTopLeft + reachableSpacesLargeBottomRight + reachableSpacesLargeBottomLeft)
    }

    val testInput = readInput("day21/TestInput")
    val input = readInput("day21/PuzzleInput")

    println("Advent of Code 2023 - Day 21")
    println("----------------------------")

    check(part1(testInput, 6) == 16)
    println("Solution for part1: ${part1(input, 64)}")

    println("Solution for part2: ${part2(input, 26_501_365)}")
}

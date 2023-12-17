package day11

import readInput
import transpose
import kotlin.math.abs

fun List<List<Char>>.findAllChars(char: Char): List<Pair<Int, Int>> {
    return this.flatMapIndexed { y, line ->
        line.mapIndexed { x, c ->
            if (c == char)
                x to y
            else null
        }.filterNotNull()
    }
}

fun Pair<Int, Int>.distanceTo(other: Pair<Int, Int>): Int {
    return abs(this.first - other.first) + abs(this.second - other.second)
}

private infix fun Int.towards(to: Int): IntProgression {
    return if (this < to)
        IntProgression.fromClosedRange(this, to, 1)
    else
        IntProgression.fromClosedRange(this, to, -1)
}

fun Pair<Int, Int>.distanceToWithSpace(
    other: Pair<Int, Int>,
    colsWithSpace: List<Int>,
    rowsWithOnlySpace: List<Int>,
    multiplySpace: Long = 2
): Long {
    val emptyRowsBetween = (this.first towards other.first).count { it in rowsWithOnlySpace }
    val emptyColsBetween = (this.second towards other.second).count { it in colsWithSpace }
    return this.distanceTo(other) + emptyColsBetween * (multiplySpace - 1) + emptyRowsBetween * (multiplySpace - 1)
}

fun main() {
    fun part1(input: List<String>): Int {
        val galaxyMap = input.map { it.toCharArray().toList() }.toList()

        val colsWithSpace = galaxyMap.mapIndexedNotNull { index, chars -> index.takeIf { chars.all { it == '.' } } }

        val rowsWithOnlySpace =
            galaxyMap.transpose().mapIndexedNotNull { index, chars -> index.takeIf { chars.all { it == '.' } } }

        val galaxies = galaxyMap.findAllChars('#')

        val distances = galaxies.flatMap { firstGalaxy ->
            galaxies.map { secondGalaxy ->
                firstGalaxy.distanceToWithSpace(secondGalaxy, colsWithSpace, rowsWithOnlySpace).toInt()
            }
        }

        return distances.sum() / 2
    }

    fun part2(input: List<String>): Long {
        val galaxyMap = input.map { it.toCharArray().toList() }.toList()

        val colsWithSpace = galaxyMap.mapIndexedNotNull { index, chars -> index.takeIf { chars.all { it == '.' } } }

        val rowsWithOnlySpace =
            galaxyMap.transpose().mapIndexedNotNull { index, chars -> index.takeIf { chars.all { it == '.' } } }

        val galaxies = galaxyMap.findAllChars('#')

        val distances = galaxies.flatMap { firstGalaxy ->
            galaxies.map { secondGalaxy ->
                firstGalaxy.distanceToWithSpace(secondGalaxy, colsWithSpace, rowsWithOnlySpace, 1_000_000L)
            }
        }

        return distances.sum() / 2
    }

    val testInput = readInput("day11/TestInput")
    val input = readInput("day11/PuzzleInput")

    println("Advent of Code 2023 - Day 11")
    println("----------------------------")

    check(part1(testInput) == 374)
    println("Solution for part1: ${part1(input)}")

    check(part2(testInput) == 82000210L)
    println("Solution for part2: ${part2(input)}")
}

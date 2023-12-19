package day18

import readInput

typealias Position = Pair<Long, Long>
typealias Direction = Pair<Long, Long>

val UP = 0L to -1L
val DOWN = 0L to 1L
val RIGHT = 1L to 0L
val LEFT = -1L to 0L

fun main() {
    data class Instruction(val direction: Direction, val distance: Long)

    fun String.toDirection(): Direction {
        return when (this) {
            "U", "3" -> UP
            "D", "1" -> DOWN
            "R", "0" -> RIGHT
            "L", "2" -> LEFT
            else -> error("Could not map char $this to a direction.")
        }
    }

    operator fun Pair<Long, Long>.plus(other: Pair<Long, Long>): Pair<Long, Long> {
        return this.first + other.first to this.second + other.second
    }

    operator fun Pair<Long, Long>.times(other: Long): Pair<Long, Long> {
        return this.first * other to this.second * other
    }

    fun calculateCorners(instructions: List<Instruction>): List<Position> {
        return instructions.fold(mutableListOf(0L to 0L)) { acc, instruction ->
            acc += acc.last() + instruction.direction * (instruction.distance)
            acc
        }
    }

    fun calculateAreaInside(corners: List<Position>): Long {
        return corners.windowed(2, 1).sumOf { (a, b) ->
            a.first * b.second - a.second * b.first
        }
    }

    fun calculateBorderSize(instructions: List<Instruction>) = instructions.sumOf { it.distance }

    fun calculateFullArea(areaInside: Long, borderSize: Long) = (areaInside + borderSize) / 2 + 1

    fun part1(input: List<String>): Long {
        val instructions = input.map { line ->
            val split = line.split(" ")
            Instruction(split[0].toDirection(), split[1].toLong())
        }

        val corners = calculateCorners(instructions)
        val areaInside = calculateAreaInside(corners)
        val borderSize = calculateBorderSize(instructions)

        return calculateFullArea(areaInside, borderSize)
    }

    fun part2(input: List<String>): Long {
        val instructions = input.map { line ->
            val hexString = line.substringAfter("#").dropLast(1)

            val distance = hexString.dropLast(1).toLong(16)
            val direction = hexString.takeLast(1).toDirection()
            Instruction(direction, distance)
        }

        val corners = calculateCorners(instructions)
        val areaInside = calculateAreaInside(corners)
        val borderSize = calculateBorderSize(instructions)

        return calculateFullArea(areaInside, borderSize)
    }

    val testInput = readInput("day18/TestInput")
    val input = readInput("day18/PuzzleInput")

    println("Advent of Code 2023 - Day 18")
    println("----------------------------")

    check(part1(testInput) == 62L)
    println("Solution for part1: ${part1(input)}")

    check(part2(testInput) == 952408144115L)
    println("Solution for part2: ${part2(input)}")
}

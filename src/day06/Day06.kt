package day06

import readInput
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    fun higherCeil(number: Double): Double {
        return if (ceil(number) == number) ceil(number) + 1 else ceil(number)
    }

    fun lowerFloor(number: Double): Double {
        return if (floor(number) == number) floor(number) - 1 else floor(number)
    }

    data class Race(val time: Long, val recordDistance: Long)

    fun calculateAmountOfWaysToWin(race: Race): Int {
        val d = sqrt(race.time.toDouble().pow(2) - 4 * race.recordDistance)
        val bottom = higherCeil((race.time - d) / 2).toInt()
        val top = lowerFloor((race.time + d) / 2).toInt()
        return top - bottom + 1
    }

    fun part1(input: List<String>): Int {
        val times = input[0].removePrefix("Time:").trim().split("\\s+".toRegex()).map { it.toLong() }
        val recordDistances = input[1].removePrefix("Distance:").trim().split("\\s+".toRegex()).map { it.toLong() }

        val races = times.zip(recordDistances).map { (time, recordDistance) ->
            Race(time, recordDistance)
        }

        return races.map(::calculateAmountOfWaysToWin).reduce { acc, amount -> acc * amount }
    }

    fun part2(input: List<String>): Int {
        val time = input[0].removePrefix("Time:").replace("\\s".toRegex(), "").toLong()
        val recordDistance = input[1].removePrefix("Distance:").replace("\\s".toRegex(), "").toLong()

        val race = Race(time, recordDistance)

        return calculateAmountOfWaysToWin(race)
    }

    val testInput = readInput("day06/TestInput")
    val input = readInput("day06/PuzzleInput")

    println("Advent of Code 2023 - Day 06")
    println("----------------------------")

    check(part1(testInput) == 288)
    println("Solution for part1: ${part1(input)}")

    check(part2(testInput) == 71503)
    println("Solution for part2: ${part2(input)}")
}

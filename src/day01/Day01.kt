package day01

import readInput

fun main() {
    fun getCalibrationValue(line: String): Int {
        val firstDigit = line.first { it.isDigit() }
        val lastDigit = line.last { it.isDigit() }
        return "$firstDigit$lastDigit".toInt()
    }

    fun part1(input: List<String>): Int = input.sumOf { getCalibrationValue(it) }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            getCalibrationValue(
                line.replace("one", "one1one")
                    .replace("two", "two2two")
                    .replace("three", "three3three")
                    .replace("four", "four4four")
                    .replace("five", "five5five")
                    .replace("six", "six6six")
                    .replace("seven", "seven7seven")
                    .replace("eight", "eight8eight")
                    .replace("nine", "nine9nine")
            )
        }
    }

    val testInputPart1 = readInput("day01/TestInputPart1")
    val testInputPart2 = readInput("day01/TestInputPart2")
    val input = readInput("day01/PuzzleInput")

    println("Advent of Code 2023 - Day 01")
    println("----------------------------")

    check(part1(testInputPart1) == 142)
    println("Solution for part1: ${part1(input)}")

    check(part2(testInputPart2) == 281)
    println("Solution for part2: ${part2(input)}")
}

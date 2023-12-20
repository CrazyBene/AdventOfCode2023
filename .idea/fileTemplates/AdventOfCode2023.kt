#set($FileName = ${NAME}) 
#set($AdventDay = $FileName.substring(3))
#set($AdventDayLength = $AdventDay.length())
package day$AdventDay

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("day${AdventDay}/TestInput")
    val input = readInput("day${AdventDay}/PuzzleInput")

    println("Advent of Code 2023 - Day $AdventDay")
    println("----------------------------")

    check(part1(testInput) == 0)
    println("Solution for part1: ${DS}{part1(input)}")

    check(part2(testInput) == 0)
    println("Solution for part2: ${DS}{part2(input)}")
}

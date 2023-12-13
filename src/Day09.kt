fun main() {
    fun parseInput(input: List<String>): List<List<Int>> {
        return input.map { line ->
            line.split(" ").map { it.toInt() }
        }
    }

    fun calculateValuePyramid(valueHistory: List<Int>): List<List<Int>> {
        val valuePyramid = mutableListOf<List<Int>>()
        valuePyramid.add(valueHistory)

        while (valuePyramid.last().any { it != 0 }) {
            valuePyramid.add(
                valuePyramid.last().zipWithNext().map { it.second - it.first }
            )
        }
        return valuePyramid
    }

    fun part1(input: List<String>): Int {
        val valueHistories = parseInput(input)

        return valueHistories.sumOf { valueHistory ->
            calculateValuePyramid(valueHistory).sumOf { it.last() }
        }
    }

    fun part2(input: List<String>): Int {
        val valueHistories = parseInput(input)

        return valueHistories.sumOf { valueHistory ->
            calculateValuePyramid(valueHistory.reversed()).sumOf { it.last() }
        }
    }

    val testInput = readInput("Day09Test")
    val input = readInput("Day09")

    println("Advent of Code 2023 - Day 09")
    println("----------------------------")

    check(part1(testInput) == 114)
    println("Solution for part1: ${part1(input)}")

    check(part2(testInput) == 2)
    println("Solution for part2: ${part2(input)}")
}

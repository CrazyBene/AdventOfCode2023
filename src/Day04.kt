import kotlin.math.min
import kotlin.math.pow

fun main() {
    fun parseInput(input: List<String>): List<Pair<List<Int>, List<Int>>> {
        return input.map { line ->
            val allNumbers = line.split(':')[1]

            val (winningNumbers, myNumbers) = allNumbers.split('|').map { numberString ->
                numberString.trim().split("\\s+".toRegex()).map { it.toInt() }
            }

            winningNumbers to myNumbers
        }
    }

    fun <T> List<T>.numberOfMatches(otherList: List<T>): Int {
        return this.filter {
            otherList.contains(it)
        }.size
    }

    fun calculatePoints(winningNumbers: List<Int>, myNumbers: List<Int>): Int {
        val matches = myNumbers.numberOfMatches(winningNumbers)
        return if (matches == 0) 0 else 2.0.pow(matches - 1).toInt()
    }

    fun part1(input: List<String>): Int {
        return parseInput(input).sumOf { (winningNumbers, myNumbers) ->
            calculatePoints(winningNumbers, myNumbers)
        }
    }

    fun part2(input: List<String>): Int {
        val numberOfCards = input.indices.map { 1 }.toMutableList()
        parseInput(input).forEachIndexed { cardIndex, (winningNumbers, myNumbers) ->
            val matches = myNumbers.numberOfMatches(winningNumbers)

            for(i in cardIndex + 1 .. min(cardIndex + matches, numberOfCards.size - 1)) {
                numberOfCards[i] += numberOfCards[cardIndex]
            }
        }
        return numberOfCards.sum()
    }

    val testInput = readInput("Day04Test")
    val input = readInput("Day04")

    println("Advent of Code 2023 - Day 4")
    println("---------------------------")

    check(part1(testInput) == 13)
    println("Solution for part1: ${part1(input)}")

    check(part2(testInput) == 30)
    println("Solution for part2: ${part2(input)}")
}

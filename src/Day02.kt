fun main() {
    data class Round(val numberOfReds: Int, val numberOfGreens: Int, val numberOfBlues: Int)

    data class Game(val id: Int, val rounds: List<Round>)

    fun String.toRound(): Round {
        val numberOfReds = "(\\d+) red".toRegex().find(this)?.groupValues?.get(1)?.toIntOrNull() ?: 0
        val numberOfGreens = "(\\d+) green".toRegex().find(this)?.groupValues?.get(1)?.toIntOrNull() ?: 0
        val numberOfBlues = "(\\d+) blue".toRegex().find(this)?.groupValues?.get(1)?.toIntOrNull() ?: 0
        return Round(numberOfReds, numberOfGreens, numberOfBlues)
    }

    fun String.toGame(): Game {
        val (gameString, roundStrings) = this.split(":")
        val gameId = gameString.split("Game")[1].trim().toInt()
        val rounds = roundStrings.split(";").map(String::toRound)
        return Game(gameId, rounds)
    }

    fun List<String>.toGames(): List<Game> {
        return this.map { line ->
            line.toGame()
        }
    }

    fun part1(input: List<String>): Int {
        val games = input.toGames()

        return games.filter { game ->
            game.rounds.forEach { round ->
                if (round.numberOfReds > 12 || round.numberOfGreens > 13 || round.numberOfBlues > 14) return@filter false
            }
            true
        }.sumOf { it.id }
    }

    fun part2(input: List<String>): Int {
        val games = input.toGames()

        return games.sumOf { game ->
            val minNumberOfReds = game.rounds.maxOf { it.numberOfReds }
            val minNumberOfGreens = game.rounds.maxOf { it.numberOfGreens }
            val minNumberOfBlues = game.rounds.maxOf { it.numberOfBlues }
            minNumberOfReds * minNumberOfGreens * minNumberOfBlues
        }
    }

    val testInput = readInput("Day02Test")
    val input = readInput("Day02")

    println("Advent of Code 2023 - Day 02")
    println("----------------------------")

    check(part1(testInput) == 8)
    println("Solution for part1: ${part1(input)}")

    check(part2(testInput) == 2286)
    println("Solution for part2: ${part2(input)}")
}

#set($FileName = ${NAME}) 
#set($AdventDay = $FileName.substring(3))
#set($AdventDayLength = $AdventDay.length())

fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day${AdventDay}Test")
    val input = readInput("Day${AdventDay}")

    println("Advent of Code 2023 - Day $AdventDay")
    println("----------------------------")

    check(part1(testInput) == 0)
    println("Solution for part1: ${DS}{part1(input)}")

    check(part2(testInput) == 0)
    println("Solution for part2: ${DS}{part2(input)}")
}

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
                line
                    .replace("one", "one1one")
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

    val testInput = readInput("Day01Test")
    val testInput2 = readInput("Day01Part2Test")
    val input = readInput("Day01")

    check(part1(testInput) == 142)
    part1(input).println()

    check(part2(testInput2) == 281)
    part2(input).println()
}

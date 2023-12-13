fun main() {
    fun List<String>.transpose() = this.map { it.toList() }.transpose().map { it.joinToString("") }

    fun List<String>.toPatterns(): List<List<String>> {
        return this.fold(mutableListOf(mutableListOf<String>())) { acc, line ->
            if (line.isBlank())
                acc.add(mutableListOf())
            else
                acc.last().add(line)

            acc
        }.map(MutableList<String>::toList)
    }

    fun findFirstHorizontalMirror(pattern: List<String>, exclude: List<Int> = emptyList()): Int {
        (1..<pattern.size).forEach { index ->
            val linesAbove = pattern.take(index)
            val linesBelow = pattern.drop(index)

            val linesAboveToCheck = linesAbove.reversed().take(linesBelow.size).reversed()
            val linesBelowToCheck = linesBelow.take(linesAbove.size)

            if (linesAboveToCheck.reversed() == linesBelowToCheck && index !in exclude)
                return index
        }
        return 0
    }

    fun findFirstVerticalMirror(pattern: List<String>, exclude: List<Int> = emptyList()): Int {
        val transposedPattern = pattern.transpose()

        return findFirstHorizontalMirror(transposedPattern, exclude)
    }

    fun part1(input: List<String>): Int {
        val patterns = input.toPatterns()

        return patterns.sumOf { pattern ->
            val linesLeftOfMirror = findFirstVerticalMirror(pattern)
            val linesAboveMirror = findFirstHorizontalMirror(pattern)

            linesLeftOfMirror + linesAboveMirror * 100
        }
    }

    fun part2(input: List<String>): Int {
        val patterns = input.toPatterns()

        return patterns.sumOf { pattern ->
            val originalLinesLeftOfMirror = findFirstVerticalMirror(pattern)
            val originalLinesAboveMirror = findFirstHorizontalMirror(pattern)

            val patternAsSingleString = pattern.joinToString("")

            (patternAsSingleString.indices).firstNotNullOf { indexToFlip ->
                val patternAsCharArray = patternAsSingleString.toCharArray()
                patternAsCharArray[indexToFlip] = if (patternAsSingleString[indexToFlip] == '.') '#' else '.'
                val possibility = patternAsCharArray.joinToString("").chunked(pattern.first().length)

                val linesLeftOfMirror = findFirstVerticalMirror(possibility, listOf(originalLinesLeftOfMirror))
                val linesAboveMirror = findFirstHorizontalMirror(possibility, listOf(originalLinesAboveMirror))

                if (linesLeftOfMirror != 0 || linesAboveMirror != 0)
                    linesLeftOfMirror + linesAboveMirror * 100
                else
                    null
            }
        }
    }

    val testInput = readInput("Day13Test")
    val input = readInput("Day13")

    println("Advent of Code 2023 - Day 13")
    println("----------------------------")

    check(part1(testInput) == 405)
    println("Solution for part1: ${part1(input)}")

    check(part2(testInput) == 400)
    println("Solution for part2: ${part2(input)}")
}

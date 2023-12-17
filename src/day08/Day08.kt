package day08

import readInput

fun main() {
    data class Node(val name: String, val nameNodeLeft: String, val nameNodeRight: String)

    fun parseNodeInput(input: List<String>): Map<String, Node> {
        return input.associate {
            val match = "^(\\w+) = \\((\\w+), (\\w+)\\)$".toRegex().find(it)

            val variables = match?.groupValues ?: error("Parsing went wrong")
            variables[1] to Node(variables[1], variables[2], variables[3])
        }
    }

    fun moveThroughNodes(
        moves: CharArray,
        nodes: Map<String, Node>,
        startNodeName: String,
        until: (String) -> Boolean
    ): Int {
        var steps = 0
        var currentNodeName = startNodeName
        var currentMovePos = 0

        while (!until(currentNodeName)) {
            val currentMove = moves[currentMovePos]
            currentMovePos = (currentMovePos + 1) % moves.size

            val currentNode = nodes[currentNodeName]!!
            currentNodeName = if (currentMove == 'L') currentNode.nameNodeLeft else currentNode.nameNodeRight

            steps++
        }

        return steps
    }

    fun findLCM(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }

    fun part1(input: List<String>): Int {
        val moves = input[0].toCharArray()
        val nodes = parseNodeInput(input.drop(2))

        return moveThroughNodes(moves, nodes, "AAA", fun(nodeName: String) = nodeName == "ZZZ")
    }

    fun part2(input: List<String>): Long {
        val moves = input[0].toCharArray()
        val nodes = parseNodeInput(input.drop(2))

        val currentNodeNames = nodes.filter { it.key.last() == 'A' }.map { it.key }

        val stepList = currentNodeNames.map { currentNodeName ->
            moveThroughNodes(moves, nodes, currentNodeName, fun(nodeName: String) = nodeName.last() == 'Z').toLong()
        }

        return stepList.reduce { acc, l -> findLCM(acc, l) }
    }

    val testInputPart1 = readInput("day08/TestInputPart1")
    val testInputPart2 = readInput("day08/TestInputPart2")
    val input = readInput("day08/PuzzleInput")

    println("Advent of Code 2023 - Day 08")
    println("----------------------------")

    check(part1(testInputPart1) == 6)
    println("Solution for part1: ${part1(input)}")

    check(part2(testInputPart2) == 6L)
    println("Solution for part2: ${part2(input)}")
}

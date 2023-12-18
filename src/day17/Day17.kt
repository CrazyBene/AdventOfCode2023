package day17

import readInput

typealias Position = Pair<Int, Int>
typealias Direction = Pair<Int, Int>

fun main() {
    data class Node(val heatLoss: Int, val position: Position, val direction: Direction, val timesInSameDir: Int) {

        val neighbors = mutableListOf<Node>()

    }

    operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
        return this.first + other.first to this.second + other.second
    }

    fun findShortestPath(startNode: Node, stopCondition: (Node) -> Boolean): Int {
        val distances = mutableMapOf<Node, Int>()
        distances[startNode] = 0

        val queue = mutableSetOf<Node>()
        queue.add(startNode)

        while (queue.isNotEmpty()) {
            val currentNode = queue.sortedBy { distances[it] }.first()
            queue.remove(currentNode)

            if (stopCondition(currentNode)) {
                return distances[currentNode]!!
            }

            for (neighbor in currentNode.neighbors) {
                val newDistance =
                    distances.getValue(currentNode).plus(neighbor.heatLoss)

                if (newDistance < distances.getOrDefault(neighbor, Int.MAX_VALUE)) {
                    distances[neighbor] = newDistance
                    queue.add(neighbor)
                }
            }
        }

        error("Could not reach stop condition from start node.")
    }

    fun part1(input: List<String>): Int {
        val nodes = input.flatMapIndexed { row, line ->
            line.toList().flatMapIndexed { col, c ->
                val nodes = mutableListOf<Node>()
                for (direction in listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0))
                    for (timesInSameDir in 1..3)
                        nodes += Node(c.code - '0'.code, col to row, direction, timesInSameDir)
                nodes
            }
        }.toMutableList()
        val startNode = Node(0, 0 to 0, 0 to 0, 0)
        nodes.add(startNode)

        val nodesMap = nodes.associateBy { Triple(it.position, it.direction, it.timesInSameDir) }

        for (node in nodes) {
            val neighbors = mutableListOf<Node?>()

            if (node.timesInSameDir < 3 && node.direction != 0 to 0) {
                neighbors += nodesMap[Triple(node.position + node.direction, node.direction, node.timesInSameDir + 1)]
            }
            if (node.direction != (1 to 0) && node.direction != (-1 to 0)) {
                neighbors += nodesMap[Triple(node.position + (1 to 0), (1 to 0), 1)]
                neighbors += nodesMap[Triple(node.position + (-1 to 0), (-1 to 0), 1)]
            }
            if (node.direction != (0 to 1) && node.direction != (0 to -1)) {
                neighbors += nodesMap[Triple(node.position + (0 to 1), (0 to 1), 1)]
                neighbors += nodesMap[Triple(node.position + (0 to -1), (0 to -1), 1)]
            }

            node.neighbors.addAll(neighbors.filterNotNull())
        }

        return findShortestPath(startNode) {
            it.position == input.first().length - 1 to input.size - 1
        }
    }

    fun part2(input: List<String>): Int {
        val nodes = input.flatMapIndexed { row, line ->
            line.toList().flatMapIndexed { col, c ->
                val nodes = mutableListOf<Node>()
                for (direction in listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0))
                    for (timesInSameDir in 1..10)
                        nodes += Node(c.code - '0'.code, col to row, direction, timesInSameDir)
                nodes
            }
        }.toMutableList()
        val startNode = Node(0, 0 to 0, 0 to 0, 0)
        nodes.add(startNode)

        val nodesMap = nodes.associateBy { Triple(it.position, it.direction, it.timesInSameDir) }

        for (node in nodes) {
            val neighbors = mutableListOf<Node?>()

            if (node.timesInSameDir < 10 && node.direction != 0 to 0) {
                neighbors += nodesMap[Triple(node.position + node.direction, node.direction, node.timesInSameDir + 1)]
            }
            if (node.timesInSameDir >= 4 || node.direction == (0 to 0)) {
                if (node.direction != (1 to 0) && node.direction != (-1 to 0)) {
                    neighbors += nodesMap[Triple(node.position + (1 to 0), (1 to 0), 1)]
                    neighbors += nodesMap[Triple(node.position + (-1 to 0), (-1 to 0), 1)]
                }
                if (node.direction != (0 to 1) && node.direction != (0 to -1)) {
                    neighbors += nodesMap[Triple(node.position + (0 to 1), (0 to 1), 1)]
                    neighbors += nodesMap[Triple(node.position + (0 to -1), (0 to -1), 1)]
                }
            }

            node.neighbors.addAll(neighbors.filterNotNull())
        }

        return findShortestPath(startNode) {
            it.position == input.first().length - 1 to input.size - 1 && it.timesInSameDir >= 4
        }
    }

    val testInput = readInput("day17/TestInput")
    val input = readInput("day17/PuzzleInput")

    println("Advent of Code 2023 - Day 17")
    println("----------------------------")

    check(part1(testInput) == 102)
    println("Solution for part1: ${part1(input)}")

    check(part2(testInput) == 94)
    println("Solution for part2: ${part2(input)}")
}

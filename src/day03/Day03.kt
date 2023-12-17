package day03

import readInput

fun main() {
    data class Position(val x: Int, val y: Int)

    data class EngineSchematicNumber(val number: Int, val startPosition: Position) {

        fun getNeighborPositions(): Set<Position> {
            val neighbors = mutableSetOf<Position>()
            for (x in startPosition.x - 1..startPosition.x + number.toString().length) {
                neighbors += Position(x, startPosition.y - 1)
                neighbors += Position(x, startPosition.y + 1)
            }
            neighbors += Position(startPosition.x - 1, startPosition.y)
            neighbors += Position(startPosition.x + number.toString().length, startPosition.y)
            return neighbors
        }

    }

    data class Symbol(val symbol: Char, val position: Position)

    fun parseInput(input: List<String>): Pair<List<EngineSchematicNumber>, List<Symbol>> {
        val engineSchematicNumbers = mutableListOf<EngineSchematicNumber>()
        val symbols = mutableListOf<Symbol>()

        input.forEachIndexed { lineNumber, line ->
            var currentNumberString = ""
            var startPosition: Position? = null
            line.forEachIndexed { colNumber, char ->
                if (char.isDigit()) {
                    currentNumberString += char
                    if (startPosition == null) startPosition = Position(colNumber, lineNumber)
                } else if (currentNumberString.isNotEmpty()) {
                    engineSchematicNumbers += EngineSchematicNumber(currentNumberString.toInt(), startPosition!!)
                    currentNumberString = ""
                    startPosition = null
                }

                if (!char.isDigit() && char != '.') symbols += Symbol(char, Position(colNumber, lineNumber))
            }
            if (currentNumberString.isNotEmpty()) {
                engineSchematicNumbers += EngineSchematicNumber(currentNumberString.toInt(), startPosition!!)
                currentNumberString = ""
                startPosition = null
            }
        }

        return engineSchematicNumbers to symbols
    }

    fun part1(input: List<String>): Int {
        val (engineSchematicNumbers, symbols) = parseInput(input)
        val symbolPositions = symbols.map { it.position }

        return engineSchematicNumbers.filter { engineSchematicNumber ->
            engineSchematicNumber.getNeighborPositions().forEach { neighbor ->
                if (symbolPositions.contains(neighbor)) return@filter true
            }
            false
        }.sumOf { it.number }
    }

    fun part2(input: List<String>): Int {
        val (engineSchematicNumbers, symbols) = parseInput(input)
        val gearSymbolPositions = symbols.filter { it.symbol == '*' }.map { it.position }

        val gearWithNeighbors = mutableMapOf<Position, List<EngineSchematicNumber>>()
        engineSchematicNumbers.forEach { engineSchematicNumber ->
            engineSchematicNumber.getNeighborPositions().forEach { neighbor ->
                if (gearSymbolPositions.contains(neighbor)) {
                    val symbolNeighbors = gearWithNeighbors[neighbor]?.toMutableList() ?: mutableListOf()
                    symbolNeighbors.add(engineSchematicNumber)
                    gearWithNeighbors[neighbor] = symbolNeighbors
                }
            }
        }

        return gearWithNeighbors.filter {
            it.value.size == 2
        }.map { (_, value) ->
            value.map { it.number }.reduce { acc, engineNumber -> acc * engineNumber }
        }.sum()
    }

    val testInput = readInput("day03/TestInput")
    val input = readInput("day03/PuzzleInput")

    println("Advent of Code 2023 - Day 03")
    println("----------------------------")

    check(part1(testInput) == 4361)
    println("Solution for part1: ${part1(input)}")

    check(part2(testInput) == 467835)
    println("Solution for part2: ${part2(input)}")
}

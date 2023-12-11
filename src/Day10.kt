data class Position(val x: Int, val y: Int) {

    operator fun plus(other: Position): Position {
        return Position(this.x + other.x, this.y + other.y)
    }

    companion object {
        val NORTH = Position(0, -1)
        val EAST = Position(1, 0)
        val SOUTH = Position(0, 1)
        val WEST = Position(-1, 0)
    }

}

fun main() {
    class Grid {

        var data: Array<CharArray>
        var width: Int
        var height: Int

        constructor(width: Int, height: Int) {
            this.width = width
            this.height = height
            data = Array(height) { CharArray(width) { '.' } }
        }

        constructor(input: List<String>) {
            data = input.map { line ->
                line.toCharArray()
            }.toTypedArray()
            this.width = data.first().size
            this.height = data.size
        }

        operator fun get(position: Position): Char {
            return data[position.y][position.x]
        }

        operator fun set(position: Position, value: Char) {
            data[position.y][position.x] = value
        }

        fun findFirst(char: Char): Position {
            data.forEachIndexed { y, line ->
                line.forEachIndexed { x, c ->
                    if (c == 'S') {
                        return Position(x, y)
                    }
                }
            }
            error("Could not find character $char in grid.")
        }

        fun findRealStartChar(): Char {
            val startPos = this.findFirst('S')
            val connectionNorth =
                ((startPos + Position.NORTH).y < this.height && (this[startPos + Position.NORTH] == '|' || this[startPos + Position.NORTH] == 'F' || this[startPos + Position.NORTH] == '7'))
            val connectionSouth =
                ((startPos + Position.SOUTH).y >= 0 && (this[startPos + Position.SOUTH] == '|' || this[startPos + Position.SOUTH] == 'L' || this[startPos + Position.SOUTH] == 'J'))
            val connectionEast =
                ((startPos + Position.EAST).x < this.width && (this[startPos + Position.EAST] == '-' || this[startPos + Position.EAST] == '7' || this[startPos + Position.EAST] == 'J'))
            val connectionWest =
                ((startPos + Position.WEST).x >= 0 && (this[startPos + Position.WEST] == '-' || this[startPos + Position.WEST] == 'L' || this[startPos + Position.WEST] == 'F'))

            return when {
                connectionNorth && connectionSouth -> '|'
                connectionNorth && connectionEast -> 'L'
                connectionNorth && connectionWest -> 'J'
                connectionSouth && connectionEast -> 'F'
                connectionSouth && connectionWest -> '7'
                connectionEast && connectionWest -> '-'
                else -> error("Error starting character not in loop?")
            }
        }

        fun findStartDirection(): Position {
            val realStartChar = this.findRealStartChar()
            return when(realStartChar) {
                '|', 'L', 'J' -> Position.NORTH
                'F', '7' -> Position.SOUTH
                else -> Position.EAST
            }
        }

        fun nextDirection(currentChar: Char, lastDirection: Position): Position {
            return when (currentChar to lastDirection) {
                '-' to Position.EAST -> Position.EAST
                '-' to Position.WEST -> Position.WEST
                '|' to Position.NORTH -> Position.NORTH
                '|' to Position.SOUTH -> Position.SOUTH
                '7' to Position.NORTH -> Position.WEST
                '7' to Position.EAST -> Position.SOUTH
                'J' to Position.SOUTH -> Position.WEST
                'J' to Position.EAST -> Position.NORTH
                'L' to Position.SOUTH -> Position.EAST
                'L' to Position.WEST -> Position.NORTH
                'F' to Position.NORTH -> Position.EAST
                'F' to Position.WEST -> Position.SOUTH
                'S' to Position.SOUTH -> Position.SOUTH
                'S' to Position.WEST -> Position.WEST
                'S' to Position.NORTH -> Position.NORTH
                'S' to Position.EAST -> Position.EAST
                else -> error("Unknown pipe tile $currentChar.")
            }
        }

        fun findLoop(): List<Position> {
            val startPos = this.findFirst('S')
            val startDirection = this.findStartDirection()

            var currentPos = startPos
            var currentDirection = startDirection
            val loopPositions = mutableListOf<Position>()
            do {
                loopPositions += currentPos
                currentDirection = nextDirection(this[currentPos], currentDirection)
                currentPos += currentDirection
            } while (this[currentPos] != 'S')

            return loopPositions
        }

    }

    fun part1(input: List<String>): Int {
        val grid = Grid(input)

        val loopPositions = grid.findLoop()

        return (loopPositions.size) / 2
    }

    fun part2(input: List<String>): Int {
        val grid = Grid(input)

        val loopPositions = grid.findLoop()
        val loopOnlyGrid = Grid(grid.width, grid.height)
        loopOnlyGrid.data.forEachIndexed { y, line ->
            line.forEachIndexed { x, _ ->
                if(loopPositions.contains(Position(x, y)))
                    loopOnlyGrid[Position(x, y)] = grid[Position(x, y)]
            }
        }
        loopOnlyGrid[grid.findFirst('S')] = grid.findRealStartChar()

        var tilesInside = 0
        loopOnlyGrid.data.forEachIndexed { y, line ->
            line.forEachIndexed inner@{ x, c ->
                if (c != '.')
                    return@inner

                var cx = x
                var cy = y
                var timesLoopCrossed = 0
                while (cx > 0 && cy > 0) {
                    val tile = loopOnlyGrid[Position(--cx, --cy)]
                    if (tile == '|' || tile == '-' || tile == 'J' || tile == 'F')
                        timesLoopCrossed++
                }
                if (timesLoopCrossed % 2 != 0) {
                    tilesInside++
                }
            }
        }

        return tilesInside
    }

    val testInputPart1 = readInput("Day10Part1Test")
    val testInputPart2 = readInput("Day10Part2Test")
    val input = readInput("Day10")

    println("Advent of Code 2023 - Day 10")
    println("----------------------------")

    check(part1(testInputPart1) == 8)
    println("Solution for part1: ${part1(input)}")

    check(part2(testInputPart2) == 4)
    println("Solution for part2: ${part2(input)}")
}

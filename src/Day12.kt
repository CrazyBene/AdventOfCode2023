import kotlin.math.pow

fun main() {
    fun countGroups(line: List<Char>): List<Int> {
        return line.joinToString(separator = "").split("""\.+""".toRegex()).map { it.count() }.filter { it != 0 }
    }

    fun part1(input: List<String>): Int {
        val lines = input.map {
            val split = it.split(" ")
            split[0] to split[1].split(",").map { it.toInt() }
        }

        val possibilityList = lines.map { (map, realCount) ->
            var possibilities = 0
            val numberOfUnknown = map.count { it == '?' }
            val tries = 2.0.pow(numberOfUnknown).toInt()
            for (i in 0 until tries) {
                val currentUnknownTry =
                    i.toString(2).padStart(numberOfUnknown, '0').map { if (it == '1') '#' else '.' }.toMutableList()
                val currentTry = map.map { if (it == '?') currentUnknownTry.removeFirst() else it }
                if (realCount == countGroups(currentTry))
                    possibilities++
            }
            possibilities
        }

        return possibilityList.sum()
    }

    fun part2(input: List<String>): Long {
        val l = input.map {
            val unknown = it.count { it == '?' } * 5 + 4
            2.0.pow(unknown).toBigDecimal()
        }

        println(l.sumOf { it })


//        val lines = input.map {
//            val split = it.split(" ")
//            split[0] to split[1].split(",").map { it.toInt() }
//        }
//
//        val possibilityList = lines.map { (map, realCount) ->
//            var possibilities = 0
//            val numberOfUnknown = map.count { it == '?'}
//            val tries = 2.0.pow(numberOfUnknown).toInt()
//            for(i in 0 until tries) {
//                val currentUnknownTry = i.toString(2).padStart(numberOfUnknown, '0').map { if(it == '1') '#' else '.' }.toMutableList()
//                val currentTry = map.map { if(it == '?') currentUnknownTry.removeFirst() else it }
//                if(realCount == countGroups(currentTry))
//                    possibilities++
//            }
//            possibilities
//        }

        return input.size.toLong()
    }

    val testInput = readInput("Day12Test")
    val input = readInput("Day12")

    println("Advent of Code 2023 - Day 12")
    println("----------------------------")

    check(part1(testInput) == 21)
    println("Solution for part1: ${part1(input)}")

    check(part2(testInput) == 525152L)
    println("Solution for part2: ${part2(input)}")
}

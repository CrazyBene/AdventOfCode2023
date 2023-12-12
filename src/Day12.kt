// I could not solve part 2 of the puzzle on my own
// I looked at the solution in this video https://www.youtube.com/watch?v=g3Ms5e7Jdqo
// and adapted it to kotlin

import kotlin.math.min

fun main() {
    val cache = mutableMapOf<Pair<String, List<Int>>, Long>()

    fun count(cfg: String, nums: List<Int>): Long {
        if (cfg.isEmpty())
            return if (nums.isEmpty()) 1 else 0

        if (nums.isEmpty())
            return if ('#' in cfg) 0 else 1

        val key = cfg to nums

        if(key in cache)
            return cache.getValue(key)

        var result = 0L

        if (cfg[0] in ".?")
            result += count(cfg.substring(1), nums)

        if (cfg[0] in "#?")
            if (nums[0] <= cfg.count() && '.' !in cfg.substring(
                    0,
                    nums[0]
                ) && (nums[0] == cfg.count() || cfg[nums[0]] != '#')
            ) {
                result += count(cfg.substring(min(cfg.count(), nums[0] + 1)), nums.subList(1, nums.size))
            }

        cache[key] = result
        return result
    }

    fun part1(input: List<String>): Long {
        val lines = input.flatMap { line ->
            line.split(" ").zipWithNext().map { (cfg, nums) ->
                cfg to nums.split(",").map { it.toInt() }
            }
        }

        return lines.sumOf {
            count(it.first, it.second)
        }
    }

    fun part2(input: List<String>): Long {
        val lines = input.flatMap { line ->
            line.split(" ").zipWithNext().map { (cfg, nums) ->
                List(5) { cfg }.joinToString("?") to List(5) { nums.split(",").map { it.toInt() } }.flatten()
            }
        }

        return lines.sumOf {
            count(it.first, it.second)
        }
    }

    val testInput = readInput("Day12Test")
    val input = readInput("Day12")

    println("Advent of Code 2023 - Day 12")
    println("----------------------------")

    check(part1(testInput) == 21L)
    println("Solution for part1: ${part1(input)}")

    check(part2(testInput) == 525152L)
    println("Solution for part2: ${part2(input)}")
}

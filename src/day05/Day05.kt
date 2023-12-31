package day05

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import readInput
import kotlin.math.min

fun main() {
    data class SourceToDestinationRange(val destinationStartRange: Long, val sourceStartRange: Long, val range: Long)

    data class SourceToDestinationMap(val sourceToDestinationRanges: List<SourceToDestinationRange>) {

        fun passItemThrough(item: Long): Long {
            sourceToDestinationRanges.forEach { map ->
                if ((map.sourceStartRange until map.sourceStartRange + map.range).contains(item)) return item - map.sourceStartRange + map.destinationStartRange
            }
            return item
        }

    }

    fun parseInputToMap(input: List<String>, name: String): SourceToDestinationMap {
        val mapStart = input.indexOfFirst { it == name }
        val ranges = input.drop(mapStart + 1).takeWhile { it.isNotBlank() }.map { line ->
            val (destinationStartRange, sourceStartRange, range) = line.split(" ").map { it.toLong() }
            SourceToDestinationRange(destinationStartRange, sourceStartRange, range)
        }
        return SourceToDestinationMap(ranges)
    }

    fun part1(input: List<String>): Long {
        val seeds = input[0].split(":")[1].trim().split("\\s+".toRegex()).map { it.toLong() }

        val seedToSoilMap = parseInputToMap(input, "seed-to-soil map:")
        val soilToFertilizerMap = parseInputToMap(input, "soil-to-fertilizer map:")
        val fertilizerToWaterMap = parseInputToMap(input, "fertilizer-to-water map:")
        val waterToLightMap = parseInputToMap(input, "water-to-light map:")
        val lightToTemperatureMap = parseInputToMap(input, "light-to-temperature map:")
        val temperatureToHumidityMap = parseInputToMap(input, "temperature-to-humidity map:")
        val humidityToLocationMap = parseInputToMap(input, "humidity-to-location map:")

        return seeds.asSequence()
            .map { seedToSoilMap.passItemThrough(it) }
            .map { soilToFertilizerMap.passItemThrough(it) }
            .map { fertilizerToWaterMap.passItemThrough(it) }
            .map { waterToLightMap.passItemThrough(it) }
            .map { lightToTemperatureMap.passItemThrough(it) }
            .map { temperatureToHumidityMap.passItemThrough(it) }
            .map { humidityToLocationMap.passItemThrough(it) }.toList().min()
    }

    fun part2(input: List<String>): Long {
        val seedToSoilMap = parseInputToMap(input, "seed-to-soil map:")
        val soilToFertilizerMap = parseInputToMap(input, "soil-to-fertilizer map:")
        val fertilizerToWaterMap = parseInputToMap(input, "fertilizer-to-water map:")
        val waterToLightMap = parseInputToMap(input, "water-to-light map:")
        val lightToTemperatureMap = parseInputToMap(input, "light-to-temperature map:")
        val temperatureToHumidityMap = parseInputToMap(input, "temperature-to-humidity map:")
        val humidityToLocationMap = parseInputToMap(input, "humidity-to-location map:")


        val seedRanges = input[0].split(":")[1].trim().split("\\s+".toRegex()).map { it.toLong() }.windowed(2, 2)

        return runBlocking(Dispatchers.Default) {
            val smallestLocations = seedRanges.map { (start, range) ->
                async {
                    var smallestLocation = Long.MAX_VALUE
                    for (seed in start until start + range) {
                        val location =
                            humidityToLocationMap.passItemThrough(
                                temperatureToHumidityMap.passItemThrough(
                                    lightToTemperatureMap.passItemThrough(
                                        waterToLightMap.passItemThrough(
                                            fertilizerToWaterMap.passItemThrough(
                                                soilToFertilizerMap.passItemThrough(
                                                    seedToSoilMap.passItemThrough(seed)
                                                )
                                            )
                                        )
                                    )
                                )
                            )

                        smallestLocation = min(location, smallestLocation)
                    }
                    smallestLocation
                }
            }.awaitAll()
            smallestLocations.min()
        }
    }

    val testInput = readInput("day05/TestInput")
    val input = readInput("day05/PuzzleInput")

    println("Advent of Code 2023 - Day 05")
    println("----------------------------")

    check(part1(testInput) == 35L)
    println("Solution for part1: ${part1(input)}")

    check(part2(testInput) == 46L)
    println("Solution for part2: ${part2(input)}")
}

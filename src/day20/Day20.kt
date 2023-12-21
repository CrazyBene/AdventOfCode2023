package day20

import readInput
import java.util.LinkedList

enum class PulseValue {
    LOW, HIGH
}

data class Pulse(val from: String, val to: String, val value: PulseValue)

abstract class Module(val name: String, val nextModules: List<String>) {

    abstract fun processPulse(incomingPulse: Pulse): List<Pulse>

}

class FlipFlopModule(name: String, nextModules: List<String>) : Module(name, nextModules) {

    enum class State {
        OFF, ON;

        fun reverse(): State {
            return if (this == ON) OFF else ON
        }
    }

    private var state = State.OFF

    override fun processPulse(incomingPulse: Pulse): List<Pulse> {
        val (_, _, pulse) = incomingPulse
        if (pulse == PulseValue.HIGH) return emptyList()

        state = state.reverse()

        val sendPulseValue = if (state == State.ON) PulseValue.HIGH else PulseValue.LOW
        return nextModules.map { Pulse(name, it, sendPulseValue) }
    }

    override fun toString(): String {
        return "FliFlopModule=(name = $name, state = $state, nextModules = $nextModules)"
    }

}

class ConjunctionModule(name: String, nextModules: List<String>) : Module(name, nextModules) {

    private val previousModulePulses = mutableMapOf<String, PulseValue>()

    override fun processPulse(incomingPulse: Pulse): List<Pulse> {
        val (from, _, pulse) = incomingPulse
        previousModulePulses[from] = pulse

        val sendPulseValue =
            if (previousModulePulses.all { it.value == PulseValue.HIGH }) PulseValue.LOW else PulseValue.HIGH
        return nextModules.map { Pulse(name, it, sendPulseValue) }
    }

    fun addPreviousModule(module: String) {
        previousModulePulses[module] = PulseValue.LOW
    }

    override fun toString(): String {
        return "ConjunctionModule=(name = $name, previousModulePulses = $previousModulePulses, nextModules = $nextModules)"
    }

}

class BroadcastModule(name: String, nextModules: List<String>) : Module(name, nextModules) {

    override fun processPulse(incomingPulse: Pulse): List<Pulse> {
        val (_, _, pulse) = incomingPulse
        return nextModules.map { Pulse(name, it, pulse) }
    }

    override fun toString(): String {
        return "BroadcastModule=(name = $name, nextModules = $nextModules)"
    }

}

fun main() {
    fun List<String>.toModules(): Map<String, Module> {
        val modules = this.map { line ->
            val type = line.first()
            val name = line.substringBefore("->").trim()
            val nextModules = line.substringAfter("->").split(",").map { it.trim() }

            return@map when (type) {
                '%' -> FlipFlopModule(name.drop(1), nextModules)
                '&' -> ConjunctionModule(name.drop(1), nextModules)
                else -> BroadcastModule(name, nextModules)
            }
        }.associateBy { it.name }

        modules.forEach { (name, module) ->
            module.nextModules.forEach inner@{
                val next = modules[it] ?: return@inner
                if (next is ConjunctionModule) {
                    next.addPreviousModule(name)
                }
            }
        }

        return modules
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
        val modules = input.toModules()

        val pulsesSend = mutableMapOf(PulseValue.LOW to 0, PulseValue.HIGH to 0)

        for (i in 0 until 1000) {
            val pulses = LinkedList(listOf(Pulse("button", "broadcaster", PulseValue.LOW)))

            while (pulses.isNotEmpty()) {
                val sendPulse = pulses.pop()

                pulsesSend[sendPulse.value] = pulsesSend[sendPulse.value]!! + 1

                val module = modules[sendPulse.to] ?: continue
                val nextPulses = module.processPulse(sendPulse)
                pulses.addAll(nextPulses)
            }
        }
        return pulsesSend.values.reduce { acc, i -> acc * i }
    }

    // small help from https://www.youtube.com/watch?v=lxm6i21O83k
    // making the same 2 assumptions, then checking if the 4 modules before the penultimate module have a cycle
    // then just implementing an lcm for that
    fun part2(input: List<String>): Long {
        val modules = input.toModules()

        val penultimateModuleName = modules.filter {
            "rx" in it.value.nextModules
        }.map { it.key }.first()

        val beforePenultimateModuleNames = modules.filter {
            penultimateModuleName in it.value.nextModules
        }.map { it.key }
        val beforePenultimateModuleCycles = mutableMapOf<String, Long>()

        var numberOfButtonPresses = 0L
        for (i in 1 .. Long.MAX_VALUE) {
            numberOfButtonPresses++

            val pulses = LinkedList(listOf(Pulse("button", "broadcaster", PulseValue.LOW)))
            while (pulses.isNotEmpty()) {
                val sendPulse = pulses.pop()

                if (beforePenultimateModuleNames.any { it == sendPulse.from }
                    && sendPulse.value == PulseValue.HIGH
                    && !beforePenultimateModuleCycles.containsKey(sendPulse.from)) {
                    beforePenultimateModuleCycles[sendPulse.from] = numberOfButtonPresses

                    if (beforePenultimateModuleCycles.keys.containsAll(beforePenultimateModuleNames))
                        return beforePenultimateModuleCycles.values.reduce { acc, l -> findLCM(acc, l) }
                }

                val module = modules[sendPulse.to] ?: continue
                val nextPulses = module.processPulse(sendPulse)
                pulses.addAll(nextPulses)
            }
        }
        error("Could not find a solution in ${Long.MAX_VALUE} button presses.")
    }

    val testInput = readInput("day20/TestInput")
    val input = readInput("day20/PuzzleInput")

    println("Advent of Code 2023 - Day 20")
    println("----------------------------")

    check(part1(testInput) == 11687500)
    println("Solution for part1: ${part1(input)}")

    println("Solution for part2: ${part2(input)}")
}

package day19

import readInput
import kotlin.math.max
import kotlin.math.min

fun main() {
    data class Rule(val scoreToCheck: Char, val checkSymbol: Char, val checkValue: Int, val nextStep: String)

    data class Workflow(val name: String, val rules: List<Rule>)

    data class Part(val scores: Map<Char, Int>) {

        val score = scores.values.sum()

        fun doesRuleApply(rule: Rule): Boolean {
            if (rule.scoreToCheck == '-') return true

            val valueToCheck = scores.getValue(rule.scoreToCheck)

            return if (rule.checkSymbol == '<') valueToCheck < rule.checkValue
            else valueToCheck > rule.checkValue

        }

        fun runThroughWorkflows(workflows: Map<String, Workflow>): String {
            var currentWorkflowName = "in"
            while (currentWorkflowName != "A" && currentWorkflowName != "R") {
                val currentWorkflow = workflows.getValue(currentWorkflowName)
                currentWorkflowName = currentWorkflow.rules.first {
                    doesRuleApply(it)
                }.nextStep
            }
            return currentWorkflowName
        }

    }

    data class PartRange(val scoreRanges: MutableMap<Char, IntRange>) {

        fun numberOfCombinations() = scoreRanges.values.map { it.count().toLong() }.reduce { acc, value -> acc * value }

        fun runThroughWorkflow(workflow: Workflow): Map<PartRange, String> {
            val nextStep = mutableMapOf<PartRange, String>()

            var partRangeStillInWorkflow = this
            for (rule in workflow.rules) {
                if (rule.scoreToCheck == '-') {
                    nextStep[partRangeStillInWorkflow] = rule.nextStep
                    continue
                }

                val allowedRuleRange =
                    if (rule.checkSymbol == '<') (1..<rule.checkValue) else (rule.checkValue + 1..4000)
                val partRangeRange = partRangeStillInWorkflow.scoreRanges.getValue(rule.scoreToCheck)

                val intersectionRange = max(allowedRuleRange.first, partRangeRange.first)..min(
                    allowedRuleRange.last, partRangeRange.last
                )
                val restRange = partRangeRange.filter { it !in intersectionRange }
                    .min()..partRangeRange.filter { it !in intersectionRange }.max()

                val partRangeThatFullFillsRule = PartRange(partRangeStillInWorkflow.scoreRanges.toMutableMap())
                partRangeThatFullFillsRule.scoreRanges[rule.scoreToCheck] = intersectionRange
                nextStep[partRangeThatFullFillsRule] = rule.nextStep

                val partRangeThatDoesNotFullFillRule = PartRange(partRangeStillInWorkflow.scoreRanges.toMutableMap())
                partRangeThatDoesNotFullFillRule.scoreRanges[rule.scoreToCheck] = restRange
                partRangeStillInWorkflow = partRangeThatDoesNotFullFillRule
            }
            return nextStep
        }

    }

    fun String.toWorkflow(): Workflow {
        val name = this.substringBefore('{')

        val rules = this.substringAfter('{').dropLast(1).split(',').map rule@{ ruleString ->
            if (':' !in ruleString) return@rule Rule('-', '-', 0, ruleString)

            val conditionString = ruleString.substringBefore(':')
            val nextStep = ruleString.substringAfter(':')
            Rule(conditionString[0], conditionString[1], conditionString.substring(2).toInt(), nextStep)
        }
        return Workflow(name, rules)
    }

    fun List<String>.toWorkflows(): List<Workflow> {
        return this.map { it.toWorkflow() }
    }

    fun String.toPart(): Part {
        val partScores = this.drop(1).dropLast(1).split(',').map { it.substring(2).toInt() }
        return Part(mapOf('x' to partScores[0], 'm' to partScores[1], 'a' to partScores[2], 's' to partScores[3]))
    }

    fun List<String>.toParts(): List<Part> {
        return this.map { it.toPart() }
    }

    fun part1(input: List<String>): Int {
        val workflows = input.takeWhile { it.isNotBlank() }.toWorkflows().associateBy { it.name }
        val parts = input.takeLastWhile { it.isNotBlank() }.toParts()

        return parts.filter { part ->
            part.runThroughWorkflows(workflows) == "A"
        }.sumOf { it.score }
    }

    fun part2(input: List<String>): Long {
        val workflows = input.takeWhile { it.isNotBlank() }.toWorkflows().associateBy { it.name }

        val startPartRange = PartRange(mutableMapOf('x' to 1..4000, 'a' to 1..4000, 'm' to 1..4000, 's' to 1..4000))

        var notFinishedPartRanges = mapOf(startPartRange to "in")
        val finishedPartRanges = mutableMapOf<PartRange, String>()

        while (notFinishedPartRanges.isNotEmpty()) {
            val nextStep = notFinishedPartRanges.map { (partRange, workflowName) ->
                val workflow = workflows.getValue(workflowName)
                partRange.runThroughWorkflow(workflow)
            }.fold(mutableMapOf<PartRange, String>()) { acc, map ->
                acc.putAll(map)
                acc
            }

            finishedPartRanges += nextStep.filter { it.value == "A" || it.value == "R" }
            notFinishedPartRanges = nextStep.filterNot { it.value == "A" || it.value == "R" }
        }

        return finishedPartRanges.filter { it.value == "A" }.keys.sumOf {
            it.numberOfCombinations()
        }
    }

    val testInput = readInput("day19/TestInput")
    val input = readInput("day19/PuzzleInput")

    println("Advent of Code 2023 - Day 19")
    println("----------------------------")

    check(part1(testInput) == 19114)
    println("Solution for part1: ${part1(input)}")

    check(part2(testInput) == 167409079868000L)
    println("Solution for part2: ${part2(input)}")
}

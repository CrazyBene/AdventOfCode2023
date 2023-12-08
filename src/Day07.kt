enum class Type(val value: Int) {
    FIVE_OF_A_KIND(6),
    FOUR_OF_A_KIND(5),
    FULL_HOUSE(4),
    THREE_OF_A_KIND(3),
    TWO_PAIRS(2),
    ONE_PAIR(1),
    HIGH_CARD(0);

    companion object {
        fun calculateType(hand: String): Type {
            val counts = hand.groupingBy { it }.eachCount()
            val numberOfPairs = counts.filter { it.value == 2 }.count()
            val numberOfTriplets = counts.filter { it.value == 3 }.count()
            val numberOfQuadruplets = counts.filter { it.value == 4 }.count()
            val numberOfQuintuplets = counts.filter { it.value == 5 }.count()

            return when {
                numberOfQuintuplets == 1 -> FIVE_OF_A_KIND
                numberOfQuadruplets == 1 -> FOUR_OF_A_KIND
                numberOfTriplets == 1 && numberOfPairs == 1 -> FULL_HOUSE
                numberOfTriplets == 1 -> THREE_OF_A_KIND
                numberOfPairs == 2 -> TWO_PAIRS
                numberOfPairs == 1 -> ONE_PAIR
                else -> HIGH_CARD
            }
        }

        fun calculateType2(hand: String): Type {
            val counts = hand.groupingBy { it }.eachCount()
            val numberOfPairs = counts.filter { it.value == 2 }.count()
            val numberOfTriplets = counts.filter { it.value == 3 }.count()
            val numberOfQuadruplets = counts.filter { it.value == 4 }.count()
            val numberOfQuintuplets = counts.filter { it.value == 5 }.count()
            val numberOfJokers = counts.filter { it.key == 'J' }.getOrDefault('J', 0)

            return when {
                numberOfQuintuplets == 1 -> FIVE_OF_A_KIND
                numberOfQuadruplets == 1 -> {
                    if (numberOfJokers != 0) FIVE_OF_A_KIND
                    else FOUR_OF_A_KIND
                }

                numberOfTriplets == 1 && numberOfPairs == 1 -> {
                    if (numberOfJokers != 0) FIVE_OF_A_KIND
                    else FULL_HOUSE
                }

                numberOfTriplets == 1 -> {
                    if (numberOfJokers != 0) FOUR_OF_A_KIND
                    else THREE_OF_A_KIND
                }

                numberOfPairs == 2 -> {
                    when (numberOfJokers) {
                        2 -> FOUR_OF_A_KIND
                        1 -> FULL_HOUSE
                        else -> TWO_PAIRS
                    }
                }

                numberOfPairs == 1 -> {
                    if (numberOfJokers != 0) THREE_OF_A_KIND
                    else ONE_PAIR
                }
                else -> {
                    if (numberOfJokers != 0) ONE_PAIR
                    else HIGH_CARD
                }
            }
        }
    }
}

fun main() {
    data class Play(val hand: String, val bid: Int, val type: Type)

    fun Char.toCardValue(): Int {
        return when (this) {
            '2' -> 1
            '3' -> 2
            '4' -> 3
            '5' -> 4
            '6' -> 5
            '7' -> 6
            '8' -> 7
            '9' -> 8
            'T' -> 9
            'J' -> 10
            'Q' -> 11
            'K' -> 12
            'A' -> 13
            else -> 0
        }
    }

    fun Char.toCardValue2(): Int {
        return when (this) {
            'J' -> 1
            '2' -> 2
            '3' -> 3
            '4' -> 4
            '5' -> 5
            '6' -> 6
            '7' -> 7
            '8' -> 8
            '9' -> 9
            'T' -> 10
            'Q' -> 11
            'K' -> 12
            'A' -> 13
            else -> 0
        }
    }

    fun calculateScore(plays: List<Play>): Int {
        return plays.mapIndexed { index, play ->
            (index + 1) * play.bid
        }.sum()
    }

    fun part1(input: List<String>): Int {
        val plays = input.map { line ->
            val (hand, bid) = line.split(" ")
            Play(hand, bid.toInt(), Type.calculateType(hand))
        }

        val sortedPlays = plays.sortedWith { a, b ->
            if (a.type.value != b.type.value)
                return@sortedWith a.type.value compareTo b.type.value
            for (cards in a.hand.zip(b.hand)) {
                if (cards.first != cards.second)
                    return@sortedWith cards.first.toCardValue() compareTo cards.second.toCardValue()
            }
            0
        }

        return calculateScore(sortedPlays)
    }

    fun part2(input: List<String>): Int {
        val plays = input.map { line ->
            val (hand, bid) = line.split(" ")
            Play(hand, bid.toInt(), Type.calculateType2(hand))
        }

        val sortedPlays = plays.sortedWith { a, b ->
            if (a.type.value != b.type.value)
                return@sortedWith a.type.value compareTo b.type.value
            for (cards in a.hand.zip(b.hand)) {
                if (cards.first != cards.second)
                    return@sortedWith cards.first.toCardValue2() compareTo cards.second.toCardValue2()
            }
            0
        }

        return calculateScore(sortedPlays)
    }

    val testInput = readInput("Day07Test")
    val input = readInput("Day07")

    println("Advent of Code 2023 - Day 7")
    println("---------------------------")

    check(part1(testInput) == 6440)
    println("Solution for part1: ${part1(input)}")

    check(part2(testInput) == 5905)
    println("Solution for part2: ${part2(input)}")
}

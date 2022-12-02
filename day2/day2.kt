import java.io.File
import kotlin.io.println
import java.util.PriorityQueue

fun main(args: Array<String>) {
    val lines = File(args[0] ).readLines()

    // Part one answer
    val total = lines.map { parseLinePartOne(it) }
                     .map { score(it.second, it.first)}
                     .sum()
    println(total)

    // Part two answer

    val total2 = lines.map { parseLinePartTwo(it) }
                     .map { Pair(it.first, playThatGetsOutcome(it.first, it.second)) }
                     .map { score(it.second, it.first) }
                     .sum()
    println(total2)
}

fun parseLinePartOne(line: String): Pair<Play, Play> {
    val splitLine = line.split(" ")

    val otherPlay = when(splitLine[0]) {
        "A" -> Play.ROCK
        "B" -> Play.PAPER
        else -> Play.SCISSORS
    }

    val ownPlay = when(splitLine[1]) {
        "X" -> Play.ROCK
        "Y" -> Play.PAPER
        else -> Play.SCISSORS
    }

    return Pair(otherPlay, ownPlay)
}

fun parseLinePartTwo(line: String): Pair<Play, Outcome> {
    val splitLine = line.split(" ")

    val otherPlay = when(splitLine[0]) {
        "A" -> Play.ROCK
        "B" -> Play.PAPER
        else -> Play.SCISSORS
    }

    val ownPlay = when(splitLine[1]) {
        "X" -> Outcome.LOOSE
        "Y" -> Outcome.DRAW
        else -> Outcome.WIN
    }

    return Pair(otherPlay, ownPlay)
}

fun playThatGetsOutcome(otherPlay: Play, outcome: Outcome): Play {
    return when(outcome) {
        Outcome.DRAW -> otherPlay
        Outcome.WIN -> playThatBeats(otherPlay)
        Outcome.LOOSE -> playThatLooses(otherPlay)
    }
}

fun score(myPlay: Play, otherPlay: Play): Int {
    val shapeScore = when(myPlay) {
        Play.ROCK -> 1
        Play.PAPER -> 2
        Play.SCISSORS -> 3
    }

    val outcomeScore = if(beats(myPlay, otherPlay)) {
        6
    } else if (myPlay == otherPlay) {
        3
    } else {
        0
    }

    return shapeScore + outcomeScore
}

fun playThatBeats(other: Play): Play {
    return when(other) {
        Play.ROCK -> Play.PAPER
        Play.PAPER -> Play.SCISSORS
        Play.SCISSORS -> Play.ROCK
    }
}

fun playThatLooses(other: Play): Play {
    return when(other) {
        Play.ROCK -> Play.SCISSORS
        Play.PAPER -> Play.ROCK
        Play.SCISSORS -> Play.PAPER
    }
}

fun beats(myPlay: Play, otherPlay: Play): Boolean {
    when(myPlay) {
        Play.ROCK -> return otherPlay == Play.SCISSORS
        Play.PAPER -> return otherPlay == Play.ROCK
        Play.SCISSORS -> return otherPlay == Play.PAPER
    }
}

enum class Play {
    ROCK, PAPER, SCISSORS
}

enum class Outcome {
    WIN, LOOSE, DRAW
}


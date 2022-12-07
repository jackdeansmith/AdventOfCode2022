import java.io.File
import kotlin.io.println

fun main(args: Array<String>) {
    val lines = File(args[0] ).readLines()

    val ans1 = lines.map { parseLine(it) }
                .filter { oneContainsOther(it.first, it.second) }
                .count()
    println(ans1)

    val ans2 = lines.map { parseLine(it) }
    .filter { overlapsAtAll(it.first, it.second) }
    .count()
    println(ans2)
}

fun Pair<Int, Int>.contains(other: Int): Boolean {
    val (r1, r2) = this
    return r1 <= other && other <= r2
}

fun Pair<Int, Int>.fullyContains(other: Pair<Int, Int>): Boolean {
    val (r1, r2) = other
    return this.contains(r1) && this.contains(r2)
}

fun oneContainsOther(range1: Pair<Int, Int>, range2: Pair<Int, Int>): Boolean {
    return range1.fullyContains(range2) || range2.fullyContains(range1)
}

fun overlapsAtAll(range1: Pair<Int, Int>, range2: Pair<Int, Int>) : Boolean {
    return range1.contains(range2.first) || range1.contains(range2.second) ||
    range2.contains(range1.first) || range2.contains(range1.second)
}

fun parseLine(line: String): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    val firstSpecString = line.split(",")[0]
    val secondSpecString = line.split(",")[1]

    val firstPairLower = firstSpecString.split("-")[0].toInt()
    val firstPairUpper = firstSpecString.split("-")[1].toInt()

    val secondPairLower = secondSpecString.split("-")[0].toInt()
    val secondPairUpper = secondSpecString.split("-")[1].toInt()

    return Pair(Pair(firstPairLower,firstPairUpper), Pair(secondPairLower,secondPairUpper))
}
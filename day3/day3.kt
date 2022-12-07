import java.io.File
import kotlin.io.println

fun main(args: Array<String>) {
    val lines = File(args[0] ).readLines()

    val ans1 = lines.map { parseLine(it) }
                    .map{ misplacedItem(it) }
                    .map { priority(it) }
                    .sum()
    println(ans1)

    val ans2 = lines.map { parseLine(it) }
                .chunked(3)
                .map { badge(it)}
                .map { priority(it) }
                .sum()
    println(ans2)
}

fun parseLine(line: String): List<Char> {
    return line.toList()
}

fun badge(sacks: List<List<Char>>): Char {
    return sacks.map {it.toSet()}
                .reduce {a, b -> a intersect b}
                .first()
}

fun misplacedItem(items: List<Char>): Char {
    val firstHalf = items.slice(0..items.size/2-1).toSet()
    val secondHalf = items.slice(items.size/2..items.size-1).toSet()
    val misplaced = (firstHalf intersect secondHalf).first()
    return misplaced
}

fun priority(item: Char): Int {
    val code = item.code

    if(item.isUpperCase()) {
        return code - 'A'.code + 27
    } else {
        return code - 'a'.code + 1
    }
}
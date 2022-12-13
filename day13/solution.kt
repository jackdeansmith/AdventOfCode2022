import java.io.File
import kotlin.io.println
import kotlin.math.abs

sealed class PacketPart: Comparable<PacketPart> {
    data class IntPart(val number: Int) : PacketPart()
    data class ListPart(val list: List<PacketPart>) : PacketPart()

    override fun compareTo(other: PacketPart): Int { 
        return isBefore(this, other)
    }
}

fun main(args: Array<String>) {
    val lines = File(args[0]).readLines()

    // val ans1 = parseProblem(lines)
    //             .withIndex()
    //             .filter {(_, packetpair) -> isBefore(packetpair.first, packetpair.second) == -1}
    //             .map {(idx, _) -> idx + 1}
    //             .sum()
    // println(ans1)

    var lines2 = lines.toMutableList()
    lines2.add("")
    lines2.add("[[2]]")
    lines2.add("[[6]]")

    val sortedPackets = parseProblemPartTwo(lines2)
               .sorted()

    val idxDivider1 = sortedPackets.withIndex().find { (_, packet) -> 
        val res = isBefore(packet, parsePacket("[[2]]"))
        res == 0
    }!!.index + 1
    val idxDivider2 = sortedPackets.withIndex().find { (_, packet) -> 
        val res = isBefore(packet, parsePacket("[[6]]"))
        res == 0
    }!!.index + 1
    println(idxDivider1 * idxDivider2)
}

fun isBefore(first: PacketPart, second: PacketPart): Int {
    if(first is PacketPart.IntPart && second is PacketPart.IntPart) {
        if(first.number < second.number) {
            return -1
        } else if (first.number == second.number) {
            return 0
        } else {
            return 1
        }
    }

    val firstListPart = if(first is PacketPart.ListPart) { first } else { PacketPart.ListPart(listOf(first))}
    val secondListPart = if(second is PacketPart.ListPart) { second } else { PacketPart.ListPart(listOf(second))}

    var idx = 0
    while(true){
        if(idx > firstListPart.list.size-1 || idx > secondListPart.list.size-1) {
            if(firstListPart.list.size < secondListPart.list.size) {
                return -1
            } else if (firstListPart.list.size == secondListPart.list.size) {
                return 0
            } else {
                return 1
            }
        }

        val f = firstListPart.list[idx]
        val s = secondListPart.list[idx]

        if(isBefore(f,s) == -1 || isBefore(f,s) == 1){
            return isBefore(f,s)
        }
        idx += 1
    }
}

fun parseProblemPartTwo(lines: List<String>): List<PacketPart> {
    return lines.chunked(3).flatMap {
        listOf(parsePacket(it[0]), parsePacket(it[1]))
    }
}

fun parseProblem(lines: List<String>): List<Pair<PacketPart, PacketPart>> {
    return lines.chunked(3).map {
        Pair(parsePacket(it[0]), parsePacket(it[1]))
    }
}

fun parsePacket(line: String): PacketPart {
    val (parts, _) = parseList(line)
    return parts
}

fun parseNumber(input: String): Pair<Int, String> {
    var line = input
    var chars = mutableListOf<Char>()
    while(line[0].isDigit()) {
        chars.add(line[0])
        line = line.slice(1..line.length-1)
    }

    return Pair(chars.joinToString("").toInt(), line)
}

// Parse first part of the list, recurse on new lists
fun parseList(input: String): Pair<PacketPart, String> {

    var line = input.slice(1..input.length-1) // remove leading [
    var parts = mutableListOf<PacketPart>()

    while(line.length > 0) {
        when(line[0]) {
            ']' -> {
                line = line.slice(1..line.length-1)
                break
            }
            '[' -> {
                val (sublist, rest) = parseList(line)
                parts.add(sublist)
                line = rest
            }
            ',' -> {
                line = line.slice(1..line.length-1)
            }
            else -> {
                val (number, rest) = parseNumber(line)
                parts.add(PacketPart.IntPart(number))
                line = rest
            }
        }
    }

    return Pair(PacketPart.ListPart(parts), line)
}
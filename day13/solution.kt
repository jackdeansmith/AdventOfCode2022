import java.io.File
import kotlin.io.println
import kotlin.math.abs

sealed class PacketPart {
    data class IntPart(val number: Int) : PacketPart()
    data class ListPart(val list: List<PacketPart>) : PacketPart()
}

fun main(args: Array<String>) {
    val lines = File(args[0]).readLines()

    println(parsePacket(tokenizeLine(lines[3])))
}

// fun parseProblem(lines: List<String>): List<Pair<PacketPart, PacketPart>> {
//     return listOf()
// }

fun parsePacket(tokens: List<String>): List<PacketPart> {
    var parts = mutableListOf<PacketPart>()

    val releventTokens = tokens.slice(1..tokens.size-2)

    // First and last token always same 
    for(tok in releventTokens) {
        println(tok)
    }

    return parts
}

fun tokenizeLine(line: String): List<String> {
    if(line == ""){
        return listOf()
    }

    var tokens = mutableListOf<String>()

    for(char in line) {
        if(char == ' '){
            continue 
        }

        if(char == '[' || char == ']'){
            tokens.add(char.toString())
            tokens.add("")
        }

        else if(char == ',') {
            tokens.add("")
        }

        else {
            tokens[tokens.size - 1] = tokens[tokens.size -1].plus(char)
        }
    }

    return tokens.filter { it != ""}
}
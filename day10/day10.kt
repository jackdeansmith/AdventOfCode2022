import java.io.File
import kotlin.io.println
import kotlin.math.abs

fun main(args: Array<String>) {
    val instructions = File(args[0]).readLines()

    val registerValues = allStates(instructions)
    val ans1 = registerValues.withIndex()
                             .map { (idx, value) -> Pair(idx+1, value)}
                             .drop(19)
                             .chunked(40)
                             .map { it.first()}
                             .map {(idx, value) -> idx * value}
                             .sum()
    println(ans1)

    val ans2 = registerValues.chunked(40)
                             .map {crtRow(it)}
                             .joinToString("\n")
    println(ans2)
}

fun crtRow(registerValues: List<Int>): String {
    var pixels = mutableListOf<Char>()

    for((idx, value) in registerValues.withIndex()) {
        val visiblePixels = setOf(value, value-1, value+1)
        if(idx in visiblePixels) {
            pixels.add('#')
        } else {
            pixels.add('.')
        }
    }

    return pixels.joinToString("")
}

fun allStates(instructions: List<String>): List<Int> {
    var registerValues: MutableList<Int> = mutableListOf(1)

    for(ins in instructions){
        registerValues.addAll(states(registerValues.last(), ins))
    }

    return registerValues
}

fun states(register: Int, instruction: String): List<Int> {
    if(instruction == "noop"){
        return listOf(register)
    }

    val incBy = instruction.split(" ")[1].toInt()
    return listOf(register, register+incBy)
}

import java.io.File
import kotlin.io.println



fun main(args: Array<String>) {
    val lines = File(args[0] ).readLines()

    var stackRows: MutableList<List<Char?>> = mutableListOf()
    var lineIndexOfStackNumbers = 0
    for ((index, line) in lines.withIndex()) {
        if(line[1] == '1'){
            lineIndexOfStackNumbers = index
            break
        }
        stackRows.add(parseStackRow(line))
    }

    var stacks: MutableList<MutableList<Char>> = mutableListOf()
    for(stackNumber in stackRows[0].indices){
        stacks.add(mutableListOf<Char>())

        for(row in stackRows.reversed()) {
            val item = row[stackNumber]
            if(item != null) {
                stacks[stackNumber].add(item)
            }
        }
    }

    var instructions: MutableList<Instruction> = mutableListOf()
    for(line in lines.slice((lineIndexOfStackNumbers + 2)..lines.size-1)){
        val instruction = parseInstructionRow(line)
        instructions.add(instruction)
    }

    part1(stacks.map {it.toMutableList()}.toMutableList(), instructions.toMutableList())
    part2(stacks.map {it.toMutableList()}.toMutableList(), instructions.toMutableList())
}

fun part1(stacks: MutableList<MutableList<Char>>, instructions: List<Instruction>) {

    for(instruction in instructions){
        stacks.apply(instruction)
    }
    val ans1 = stacks.map {it.last()}
                     .joinToString("")
    println("Answer 1: $ans1")
}

fun part2(stacks: MutableList<MutableList<Char>>, instructions: List<Instruction>) {

    for(instruction in instructions){
        stacks.apply2(instruction)
    }
    val ans2 = stacks.map {it.last()}
                     .joinToString("")
    println("Answer 2: $ans2")
}

data class Instruction(val number: Int, val fromIndex: Int, val toIndex: Int)

fun parseStackRow(line: String): List<Char?> {
    return line.toList().chunked(4)
              .map { it.filter {char -> !setOf(' ', '[', ']').contains(char)} }
              .map { it.firstOrNull()}
}

fun parseInstructionRow(line: String): Instruction {
    // move 2 from 2 to 7
    val regex = Regex("""\d+""")
    val numbers = regex.findAll(line).map { it.value.toInt() }.toList()
    return Instruction(numbers[0], numbers[1], numbers[2])
}

fun MutableList<MutableList<Char>>.move(from: Int, to: Int) {
    val item = this[from-1].removeLast()
    this[to-1].add(item)
}

fun MutableList<MutableList<Char>>.apply(instruction: Instruction) {
    repeat(instruction.number) {
        this.move(instruction.fromIndex, instruction.toIndex)
    }
}

fun MutableList<MutableList<Char>>.apply2(instruction: Instruction) {
    val lifted: MutableList<Char> = mutableListOf()
    repeat(instruction.number) {
        lifted.add(this[instruction.fromIndex-1].removeLast())
    }

    this[instruction.toIndex-1].addAll(lifted.reversed())

    // val lifted = this[instruction.fromIndex-1].reversed().slice(0..instruction.number-1)
    // this[instruction.toIndex-1].addAll(lifted.reversed())
    // println("lifted: $lifted")

    // repeat(instruction.number) {
    //     this.move(instruction.fromIndex, instruction.toIndex)
    // }
}
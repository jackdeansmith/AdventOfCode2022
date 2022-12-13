import java.io.File
import kotlin.io.println
import kotlin.math.abs
import java.util.PriorityQueue
import java.math.BigInteger

fun main(args: Array<String>) {
    val lines = File(args[0]).readLines()

    // part1(lines)
    part2(lines)
}

fun part2(lines: List<String>) {
    val monkeys = parseProblemSpec(lines)
    val worryModulo = monkeys.map {it.divisibilityTest}.reduce { a,b -> a * b}
    var iter = 0
    repeat(10000) {
        // println("Round $it")
        // println(monkeys)
        iter += 1
        for(monkey in monkeys){
            for ((target, item) in monkey.throws(1, worryModulo)) {
                monkeys[target].items.add(item)
            }
        }
    }

    val ans = monkeys.map { it.numInspected }
                       .topK(2)
                       .reduce { a, b -> a * b}
    println("Part2 $ans")
}

fun part1(lines: List<String>) {
    val monkeys = parseProblemSpec(lines)
    val worryModulo = monkeys.map {it.divisibilityTest}.reduce { a,b -> a * b}
    println("worry modulo: $worryModulo")
    repeat(20) {
        for(monkey in monkeys){
            for ((target, item) in monkey.throws(3, worryModulo)) {
                monkeys[target].items.add(item)
            }
        }
    }

    val ans1 = monkeys.map { it.numInspected }
                       .topK(2)
                       .reduce { a, b -> a * b}
    println("Part1 $ans1")
}

public fun <T : Comparable<T>> Iterable<T>.topK(k: Int): Sequence<T> {
    val iterator = iterator()
    var heap = PriorityQueue<T>()
    while (iterator.hasNext()) {
        val e = iterator.next()
        heap.add(e)
        if(heap.size > k){
            heap.poll()
        }    
    }
    return heap.iterator().asSequence()
}

data class Operation(val multiplyBy: Int, val addTo: Int, val square: Int)

fun Operation.apply(oldValue: BigInteger): BigInteger {
    return BigInteger.valueOf(square.toLong()) * (oldValue * oldValue) + (BigInteger.valueOf(multiplyBy.toLong()) * oldValue) + BigInteger.valueOf(addTo.toLong())
}

data class Monkey(var items: MutableList<BigInteger>, val operation: Operation, val divisibilityTest: Int, val trueTarget: Int, val falseTarget: Int) {
    public var numInspected: BigInteger = BigInteger("0")
}

fun Monkey.throws(worryDivisor: Int, worryModulo: Int): List<Pair<Int, BigInteger>> {
    var results = mutableListOf<Pair<Int, BigInteger>>()
    for(item in items){
        val newWorryValue = operation.apply(item)
        val withBoredom = newWorryValue / BigInteger.valueOf(worryDivisor.toLong())
        val withReduction = withBoredom % BigInteger.valueOf(worryModulo.toLong())
        // println("withBoredom: $withBoredom, withReduction: $withReduction")
        val throwsTo = if(withReduction % BigInteger.valueOf(divisibilityTest.toLong()) == BigInteger("0")) { trueTarget } else { falseTarget }
        results.add(Pair(throwsTo, withReduction))
    }

    numInspected += BigInteger.valueOf(results.size.toLong())
    items = mutableListOf()

    return results
}

fun parseProblemSpec(lines: List<String>): List<Monkey> {
    return lines.chunked(7).map { parseMonkey(it) }
}

fun parseMonkey(monkeySpec: List<String>): Monkey {
    val items = monkeySpec[1].split(":")[1].trim().split(",").map { it.trim() }.map {BigInteger(it)}.toMutableList()
    val operation = parseOperation(monkeySpec[2])
    val divisibilityTest = monkeySpec[3].split(" ").last().toInt()
    val trueTarget = monkeySpec[4].split(" ").last().toInt()
    val falseTarget = monkeySpec[5].split(" ").last().toInt()
    return Monkey(items, operation, divisibilityTest, trueTarget, falseTarget)
}

fun parseOperation(line: String): Operation {
    val opString = line.split("=")[1].trim()

    if(opString == "old * old"){
        return Operation(0, 0, 1)
    }

    val number = opString.split(" ").last().toInt()

    if(opString[4] == '+'){
        return Operation(1, number, 0)
    } else {
        return Operation(number, 0, 0)
    }
}
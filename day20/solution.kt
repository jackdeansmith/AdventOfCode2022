import java.io.File
import kotlin.io.println
import kotlin.math.abs

fun main(args: Array<String>) {
    val numbers = File(args[0]).readLines().map{it.toLong()}

    // Verified, the list contains duplicates
    // var set: MutableSet<Long> = mutableSetOf()
    // for(x in numbers) {
    //     if(set.contains(x)){
    //         println("duplicate: $x")
    //     }
    //     set.add(x)
    // }
    // println("end of list")

    // Now, numbers must be indexed to deal with this
    val indexedNumbers = numbers.withIndex().map {(idx, num) -> Pair(idx, num)}

    // Simple enough now, I need to find each number by index and move it
    var result1 = mix(indexedNumbers).map{it.second}
    val pt1 = answerFromPlaintext(result1)
    println("Part1: $pt1")

    // Part 2, multiply all numbers by decryption key
    val key = 811589153
    val newIndexedNumbers = indexedNumbers.map { Pair(it.first, key * it.second)}
    // println(newIndexedNumbers)

    var result2 = newIndexedNumbers.toMutableList()
    repeat(10) {
        result2 = mix(result2).toMutableList()
    }
    var result2Nums = result2.map{it.second}
    val pt2 = answerFromPlaintext(result2Nums)
    println("Part2: $pt2")
}

fun mix(items: List<Pair<Int, Long>>): List<Pair<Int, Long>> {
    var result = items.toMutableList()
    for(origIndex in 0..result.size-1) {

        var foundAtIndex = 0
        for(idx in 0..result.size-1) {
            foundAtIndex = idx
            if(result[foundAtIndex].first == origIndex){
                break
            }
        }

        val toBeMoved = result.removeAt(foundAtIndex)

        var newIndex = (foundAtIndex + toBeMoved.second).mod(result.size)
        if(newIndex == 0){
            newIndex = result.size
        }

        result.add(newIndex, toBeMoved)
    }
    return result
}

fun answerFromPlaintext(plaintext: List<Long>): Long {
    val indexOfZero = plaintext.indexOf(0)
    val cord1 = plaintext[(indexOfZero + 1000).mod(plaintext.size)]
    val cord2 = plaintext[(indexOfZero + 2000).mod(plaintext.size)]
    val cord3 = plaintext[(indexOfZero + 3000).mod(plaintext.size)]

    return cord1 + cord2 + cord3
}
import java.io.File
import kotlin.io.println
import kotlin.math.abs

fun main(args: Array<String>) {
    val numbers = File(args[0]).readLines().map{it.toInt()}

    // Verified, the list contains duplicates
    // var set: MutableSet<Int> = mutableSetOf()
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
    var result = indexedNumbers.toMutableList()
    // println(result)

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

        // println("moving: $origIndex, rawNewIndex: ${(foundAtIndex + toBeMoved.second)}")
        // println("newIndex: $newIndex, tobemoved: $toBeMoved")

        result.add(newIndex, toBeMoved)
        // println(result.map {it.second})

    }

    val pt1 = answerFromPlaintext(result.map{it.second})
    println("Part1: $pt1")
}

fun answerFromPlaintext(plaintext: List<Int>): Int {
    val indexOfZero = plaintext.indexOf(0)
    val cord1 = plaintext[(indexOfZero + 1000).mod(plaintext.size)]
    val cord2 = plaintext[(indexOfZero + 2000).mod(plaintext.size)]
    val cord3 = plaintext[(indexOfZero + 3000).mod(plaintext.size)]

    return cord1 + cord2 + cord3
}
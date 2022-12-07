import java.io.File
import kotlin.io.println

fun main(args: Array<String>) {
    val inputstring = File(args[0]).readText()
    println("Part1: ${posFirstDistinct(inputstring, 4)}")
    println("Part2: ${posFirstDistinct(inputstring, 14)}")
}

fun posFirstMarker(input: String): Int {
    for (idx in 3..input.length-1) {
        val last4 = input.slice(idx-3..idx)
        if(!containsDuplicates(last4)){
            return idx+1
        }
    }
    return 0
}

fun posFirstDistinct(input: String, num: Int): Int {
    for (idx in num-1..input.length-1) {
        val lastn = input.slice(idx-num+1..idx)
        if(!containsDuplicates(lastn)){
            return idx+1
        }
    }
    return 0
}

fun containsDuplicates(input: String): Boolean {
    var contents: MutableSet<Char> = mutableSetOf()
    for(x in input) {
        if(x in contents){
            return true
        }
        contents.add(x)
    }
    return false
}
import java.io.File
import kotlin.io.println
import java.util.PriorityQueue

fun main(args: Array<String>) {
    val lines = File(args[0] ).readLines()
    
    val calorieCounts = lines.fold(mutableListOf<MutableList<Int>>(mutableListOf<Int>())) {calorieCounts, line -> 
        if(line == "") {
            calorieCounts.add(mutableListOf<Int>())
        }
        else {
            calorieCounts.last().add(line.toInt())
        }
        calorieCounts
    }

    val totalCalorieCounts = calorieCounts.map { it.sum() }

    println(totalCalorieCounts.max())

    val totalCaloriesTopThree = totalCalorieCounts.topK(3).sum()
    println(totalCaloriesTopThree)
}

// Total overkill to do topK calc in N log K instead of N log N time
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
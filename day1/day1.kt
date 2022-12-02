import java.io.File
import kotlin.io.println

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

    val totalCaloriesTopThree = totalCalorieCounts.sortedDescending().take(3).sum()

    println(totalCaloriesTopThree)
}
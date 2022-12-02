import java.io.File
import kotlin.io.println

fun main(args: Array<String>) {
    val lines = File(args[0] ).readLines()

    val ans = lines.map { parseLine(it) }
    println(ans)
}

fun parseLine(line: String): TODO {

}
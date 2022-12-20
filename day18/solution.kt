import java.io.File
import kotlin.io.println
import kotlin.math.abs

fun main(args: Array<String>) {
    val lines = File(args[0]).readLines()

    var cubes: MutableSet<Triple<Int, Int, Int>> = mutableSetOf()

    for(line in lines) {
        val parts = line.split(",")
        val x = parts[0].toInt()
        val y = parts[1].toInt()
        val z = parts[2].toInt()

        cubes.add(Triple(x, y, z))
    }

    println("part1: ${surfaceArea(cubes)}")
}

fun surfaceArea(cubes: Set<Triple<Int, Int, Int>>): Int {

    var result = 0

    for(cube in cubes) {
        for(adjacentCube in adjacent(cube)){
            if(!cubes.contains(adjacentCube)) {
                result += 1
            }
        }
    }

    return result
}

fun adjacent(cube: Triple<Int, Int, Int>): List<Triple<Int, Int, Int>> {
    val (x, y, z) = cube
    var results: MutableList<Triple<Int, Int, Int>> = mutableListOf()
    for(d in listOf(-1, 1)){
        results.add(Triple(x+d, y, z))
        results.add(Triple(x, y+d, z))
        results.add(Triple(x, y, z+d))
    }
    return results
}
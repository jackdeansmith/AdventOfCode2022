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

    println("part2: ${externalSurfaceArea(cubes)}")
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

fun externalSurfaceArea(cubes: Set<Triple<Int, Int, Int>>): Int {

    var result = 0

    // Practical volume is actually very small, can store external locations as a set, DP build the external locations by expanding
    val extern = externalLocations(cubes)

    for(cube in cubes) {
        for(adjacentCube in adjacent(cube)){
            if(extern.contains(adjacentCube)) {
                result += 1
            }
        }
    }

    return result
}

fun externalLocations(cubes: Set<Triple<Int, Int, Int>>): Set<Triple<Int, Int, Int>> {

    var result: MutableSet<Triple<Int, Int, Int>> = mutableSetOf()
    val bounds = boundaries(cubes)

    val (minX, minY, minZ) = bounds.first
    val (maxX, maxY, maxZ) = bounds.second

    // terribly inefficient way of doing this, works though
    var frontier: MutableSet<Triple<Int, Int, Int>> = mutableSetOf()
    for(x in (minX - 1)..(maxX + 1)) {
        for(y in (minY - 1)..(maxY + 1)) {
            for(z in (minZ - 1)..(maxZ + 1)) {
                if(x == minX - 1 || x == maxX + 1 || y == minY -1 || y == maxY + 1 || z == minZ - 1 || z == maxZ + 1) {
                    frontier.add(Triple(x, y, z))
                }
            }
        }
    }

    while(frontier.size > 0) {
        val newFrontier = frontier.flatMap { adjacent(it) }
                              .filter {(x, y, z) -> !(x < minX || y < minY || z < minZ || x > maxX || y > maxY || z > maxZ)}
                              .filter { !result.contains(it) }
                              .filter { !cubes.contains(it) }
                              .toMutableSet()
        result.addAll(frontier)
        frontier = newFrontier
    }

    return result
}

fun boundaries(locations: Set<Triple<Int, Int, Int>>): Pair<Triple<Int, Int, Int>, Triple<Int, Int, Int>> {
    var minX = locations.map {(x, _, _) -> x}.min()
    var maxX = locations.map {(x, _, _) -> x}.max()
    var minY = locations.map {(_, y, _) -> y}.min()
    var maxY = locations.map {(_, y, _) -> y}.max()
    var minZ = locations.map {(_, _, z) -> z}.min()
    var maxZ = locations.map {(_, _, z) -> z}.max()
    return Pair(Triple(minX, minY, minZ), Triple(maxX, maxY, maxZ))
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
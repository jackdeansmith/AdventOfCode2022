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

    println("boundaries: ${boundaries(cubes)}")

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

    val boundaries = boundaries(cubes)

    for(cube in cubes) {
        for(adjacentCube in adjacent(cube)){
            if(isExternal(adjacentCube, cubes, boundaries)) {
                result += 1
            }
        }
    }

    return result
}

fun isExternal(location: Triple<Int, Int, Int>, dropLocations: Set<Triple<Int, Int, Int>>, boundaries: Pair<Triple<Int, Int, Int>, Triple<Int, Int, Int>>): Boolean {
    val (x, y, z) = location
    val (minX, minY, minZ) = boundaries.first
    val (maxX, maxY, maxZ) = boundaries.second

    // base case, a location is external if it's located out past a boundary 
    if(x < minX || y < minY || z < minZ || x > maxX || y > maxY || z > maxZ) {
        return true
    }

    // base case, a location is not external if it's part of the droplet
    if(dropLocations.contains(location)) {
        return false
    }

    // else, a location is external if any of its neighbors are external
    return adjacent(location).any {isExternal(it, dropLocations, boundaries)}
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
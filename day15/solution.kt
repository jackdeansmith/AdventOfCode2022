import java.io.File
import kotlin.io.println
import kotlin.math.abs

fun main(args: Array<String>) {
    val lines = File(args[0]).readLines()
                             .map {parseLine(it)}

    println("Part 1: ${part1(2000000, lines)}")
    
    println("Part 2:")

    // println(coverageForRow(10, Pair(Pair(8, 7), Pair(2, 10))))
}

// extracts four cordinates of line "Sensor at x=2, y=18: closest beacon is at x=-2, y=15" using above regex
// returns pair or pair of ints
fun parseLine(line: String): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    val regex = Regex("(x|y)=(-?\\d+)")
    val ans = regex.findAll(line).map { it.groupValues[2].toInt() }.toList()
    return Pair(Pair(ans[0], ans[1]), Pair(ans[2], ans[3]))
}

fun part1(row: Int, input: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>): Int {
    // For each sensor, identify the parts of the row where there can be no beacon
    // Intersect them all together

    val coverages = input.map { coverageForRow(row, it)}
    val allColsCovered = coverages.reduce { acc, set -> acc union set }

    val beaconPositions = beaconsInRow(row, input)

    // subtract the beacon positions from the set of all columns covered
    val result = allColsCovered.subtract(beaconPositions)

    return result.size
}

fun part2(bound: Int, input: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>): Pair<Int, Int> {

    var bestYForEachX: MutableList<Int> = mutableListOf()
    repeat(bound)
        bestYForEachX.add(0)
    }

    while(true) {
        var nextBestYForEachX = bestYForEachX.toMutableList()
        for(x in range 0..bound){

        }

        if(nextBestYForEachX == bestYForEachX){
            break
        }
    }

    print("terminated")

    return Pair(0,0)
}

fun tilt(point: Pair<Int, Int>): Pair<Int, Int> {
    val (x, y) = point
    return Pair(x + y, x - y)
}

fun beaconsInRow(row: Int, observations: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>): Set<Int> {
    return observations.filter { it.second.second == row }.map { it.second.first }.toSet()
}

fun coverageForRow(row: Int, observation: Pair<Pair<Int, Int>, Pair<Int, Int>>): Set<Int> {
    var result: MutableSet<Int> = mutableSetOf()

    val (sensorPos, beaconPos) = observation
    val sensorDist = manhattanDistance(sensorPos, beaconPos)

    val rowDelta = abs(sensorPos.second - row)
    val shadowSize = (sensorDist - rowDelta)
    val shadowStart = sensorPos.first - shadowSize
    val shadowEnd = sensorPos.first + shadowSize

    // add all numbers between shadowState and shadowEnd inclusive to result
    for (i in shadowStart..shadowEnd) {
        result.add(i)
    }

    return result
}

fun manhattanDistance(p1: Pair<Int, Int>, p2: Pair<Int, Int>): Int {
    return abs(p1.first - p2.first) + abs(p1.second - p2.second)
}

import java.io.File
import kotlin.io.println
import kotlin.math.abs
import java.util.PriorityQueue

typealias Position = Pair<Int, Int>

fun main(args: Array<String>) {
    val lines = File(args[0]).readLines()
    val map = parse(lines)

    // println(map)

    printLenShortestPath(map)

}

fun printLenShortestPath(map: List<List<Char>>) {

    val compareByMinDist: Comparator<Pair<Int, Position>> = compareBy { it.first }
    var unvisited = PriorityQueue<Pair<Int, Position>>(compareByMinDist)
    var minDistances: MutableMap<Position, Int> = mutableMapOf()

    for(rowIdx in 0..map.size-1) {
        for(colIdx in 0..map[0].size-1) {
            val position = Pair(rowIdx, colIdx)
            if(map[rowIdx][colIdx] == 'S'){
                unvisited.add(Pair(0, position))
                minDistances[position] = 0
            } else {
                unvisited.add(Pair(Int.MAX_VALUE, position))
                minDistances[position] = Int.MAX_VALUE
            }
        }    
    }

    while(unvisited.peek() != null) {
        var (minDistance, position) = unvisited.poll()

        if(map[position.first][position.second] == 'E'){
            println("Min distance to end: $minDistance")
            break
        }

        val neighbors = neighbors(map, position)

        for (neighbor in neighbors) { 
            val newMinDistance = minDistance + 1

            if(minDistances[neighbor]!! > newMinDistance) {
                unvisited.remove(Pair(minDistances[neighbor], neighbor))
                minDistances[neighbor] = newMinDistance
                unvisited.add(Pair(minDistances[neighbor]!!, neighbor))
            }
        }
    }
}

fun neighbors(map: List<List<Char>>, position: Position): List<Position> {    
    val possibleNeighborPositions = listOf(Pair(-1, 0), Pair(0, -1), Pair(1, 0), Pair(0,1)).map {
        Pair(it.first + position.first, it.second + position.second)
    }
    .filter {
        it.first >= 0 && it.first < map.size && it.second >= 0 && it.second < map[0].size
    }

    val charAtPos = map[position.first][position.second]

    return possibleNeighborPositions.filter {
        val destChar = map[it.first][it.second]
        destChar.elevation() <= charAtPos.elevation() + 1
    }
}

fun Char.elevation(): Int {
    return when(this) {
        'S' -> 'a'.code
        'E' -> 'z'.code
        else -> this.code
    }
}

fun parse(lines: List<String>): List<List<Char>> {
    return lines.map { it.toList() }
}
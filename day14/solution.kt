import java.io.File
import kotlin.io.println
import kotlin.math.abs

typealias Position = Pair<Int, Int>

data class State(val maxX: Int, val minX: Int, val maxY: Int, val minY: Int, var positions:MutableList<MutableList<Char>>){
    operator fun get(x: Int, y: Int): Char {
        return positions[x - minX][y - minY]
    }

    operator fun set(x: Int, y: Int, value: Char) {
        positions[x - minX][y - minY] = value
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("minX: $minX, maxX: $maxX, minY: $minY, maxY: $maxY\n")
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                sb.append(this[x, y])
                sb.append(" ")
            }
            sb.append("\n")
        }
        return sb.toString()
    }

    // return true if the sand came to rest, false if it escaped
    fun addSand(initialPosition: Position): Boolean {
        var sandPos = initialPosition

        var charAtStartPoint = this[initialPosition.first, initialPosition.second]
        if(charAtStartPoint != '.'){
            return false
        }

        while(isWithinBounds(sandPos)) {
            val newSandPos = nextSandPosition(sandPos)

            if(newSandPos == sandPos) {
                this[newSandPos.first, newSandPos.second] = 'O'
                return true
            }

            sandPos = newSandPos
        }

        return false // fall through, the sand kept moving until it left our bounds
    }

    fun isWithinBounds(pos: Position): Boolean {
        return pos.first >= minX && pos.first <= maxX && pos.second >= minY && pos.second <= maxY
    }

    fun nextSandPosition(position: Position): Position {
        val (x, y) = position

        for (pos in listOf(Pair(x, y+1), Pair(x-1, y+1), Pair(x+1, y+1))) {
            if(isWithinBounds(pos)){
                val charAtPos = this[pos.first, pos.second]
                if(charAtPos == '.'){
                    return pos
                }
            } else {
                return pos
            }
        }

        return position
    }
}

fun main(args: Array<String>) {
    val lines = File(args[0]).readLines().map { parseLine(it)}

    // println(lines)
    val iState = makeInitialState(lines)
    println(part1(iState, Pair(500,0)))

    val iStateWithFloor = makeInitialStateWithFloor(lines)
    println(part1(iStateWithFloor, Pair(500,0)))
}

fun part1(iState: State, addPos: Position): Int {
    var res = 0
    while(iState.addSand(addPos)){
        res += 1
    }
    return res
}

// Chars are "." for empty, "#" for rock, and "O" for sand
fun makeInitialState(lines: List<List<Position>>): State {
    // First, find the maximum extent of the positons in x and y
    var maxX = lines.flatten().map {it.first}.max()
    var maxY = lines.flatten().map {it.second}.max()
    var minX = lines.flatten().map {it.first}.min()
    val minY = listOf(lines.flatten().map {it.second}.min(), 0).min()

    var positions: MutableList<MutableList<Char>> = mutableListOf()

    for(x in minX..maxX) {
        var row = mutableListOf<Char>()
        for(y in minY..maxY) {
            row.add('.')
        }
        positions.add(row)
    }

    var state = State(maxX, minX, maxY, minY, positions)

    for(line in lines) {
        for(secondIdx in 1..line.size-1) {
            val firstIdx = secondIdx - 1

            val (firstX, firstY) = line[firstIdx]
            val (secondX, secondY) = line[secondIdx]

            if(firstX == secondX){
                for(y in reversibleRange(firstY, secondY)) {
                    state[firstX, y] = '#'
                }
            }
            else if(firstY == secondY) {
                for(x in reversibleRange(firstX, secondX)) {
                    state[x, firstY] = '#'
                }
            } else {
                println("PROBLEM")
            }
        }
    }

    return state
}

// Chars are "." for empty, "#" for rock, and "O" for sand
fun makeInitialStateWithFloor(lines: List<List<Position>>): State {
    // First, find the maximum extent of the positons in x and y
    var maxX = lines.flatten().map {it.first}.max()
    var maxY = lines.flatten().map {it.second}.max()
    var minX = lines.flatten().map {it.first}.min()
    val minY = listOf(lines.flatten().map {it.second}.min(), 0).min()

    val floorLevel = maxY + 2
    maxY = floorLevel

    minX = listOf(500-floorLevel-1, minX).min()
    maxX = listOf(500+floorLevel+1, maxX).max()

    var positions: MutableList<MutableList<Char>> = mutableListOf()

    for(x in minX..maxX) {
        var row = mutableListOf<Char>()
        for(y in minY..maxY) {
            row.add('.')
        }
        positions.add(row)
    }

    var state = State(maxX, minX, maxY, minY, positions)

    for(line in lines) {
        for(secondIdx in 1..line.size-1) {
            val firstIdx = secondIdx - 1

            val (firstX, firstY) = line[firstIdx]
            val (secondX, secondY) = line[secondIdx]

            if(firstX == secondX){
                for(y in reversibleRange(firstY, secondY)) {
                    state[firstX, y] = '#'
                }
            }
            else if(firstY == secondY) {
                for(x in reversibleRange(firstX, secondX)) {
                    state[x, firstY] = '#'
                }
            } else {
                println("PROBLEM")
            }
        }
    }

    // Draw the floor
    for(x in minX..maxX){
        state[x, floorLevel] = '#'
    }

    return state
}

fun reversibleRange(bound1: Int, bound2: Int): IntProgression {
    return if (bound1 <= bound2) {bound1..bound2} else {bound1 downTo bound2}
}

fun parseLine(input: String): List<Pair<Int, Int>> {
    val coordinates = input.split(" -> ")
    return coordinates.map { coord ->
        val (x, y) = coord.split(",")
        Pair(x.toInt(), y.toInt())
    }
}
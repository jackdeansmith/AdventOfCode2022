import java.io.File
import kotlin.io.println
import kotlin.math.abs

typealias Position = Pair<Int, Int>

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

fun main(args: Array<String>) {
    val instructions = File(args[0]).readLines().map {parseLine(it)}

    val ans = allTailVisitedPositionsPt1(instructions).toSet().count()
    println("pt1: $ans")

    val ans2 = allTailVisitedPositionsPt2(instructions).toSet().count()
    println("pt2: $ans2")
}

fun allTailVisitedPositionsPt2(instructions: List<Pair<Direction, Int>>): List<Position> {

    // start of rope is head, end of rope is tail
    var rope = mutableListOf<Position>()
    repeat(10) {
        rope.add(Position(0,0))
    }

    var result: MutableList<Position> = mutableListOf(rope.last())

    for ((direction, count) in instructions){
        repeat(count) {
            rope = applyMotionToRope(rope, direction).toMutableList()
            result.add(rope.last())
        }
    }

    return result
}

fun allTailVisitedPositionsPt1(instructions: List<Pair<Direction, Int>>): List<Position> {
    var headPos = Position(0,0)
    var tailPos = Position(0,0)

    var result: MutableList<Position> = mutableListOf(tailPos)

    for ((direction, count) in instructions){
        repeat(count) {
            val (nh, nt) = applyMotion(Pair(headPos, tailPos), direction)
            headPos = nh
            tailPos = nt
            result.add(tailPos)
        }
    }

    return result
}

fun parseLine(line: String): Pair<Direction, Int> {
    val parts = line.split(" ")
    
    val dir = when(parts[0]) {
        "U" -> Direction.UP
        "D" -> Direction.DOWN
        "L" -> Direction.LEFT
        "R" -> Direction.RIGHT
        else -> Direction.UP
    }

    val count = parts[1].toInt()
    return Pair(dir, count)
}

fun move(pos: Position, dir: Direction): Position {
    val (x, y) = pos
    when(dir) {
        Direction.UP -> return Position(x, y+1)
        Direction.DOWN -> return Position(x, y-1)
        Direction.LEFT -> return Position(x-1, y)
        Direction.RIGHT -> return Position(x+1, y)
    }
}

fun adjustTail(headPos: Position, tailPos: Position): Position {
    var (headX, headY) = headPos
    var (tailX, tailY) = tailPos

    val deltaX = headX - tailX
    val deltaY = headY - tailY

    // If tail is "touching" head, nothing to worry about 
    if(abs(deltaX) <= 1 && abs(deltaY) <= 1){
        return tailPos
    }

    val newTailY = tailY + if (deltaY != 0) { deltaY/abs(deltaY) } else 0
    val newTailX = tailX + if (deltaX != 0) { deltaX/abs(deltaX) } else 0
    return Position(newTailX, newTailY)
}

fun applyMotionToRope(rope: List<Position>, direction: Direction): List<Position> {
    val rope = rope.toMutableList()
    
    // Move the rope head according to the rules
    rope[0] = move(rope[0], direction)

    // Move each next element of the rope
    for(idx in 0..rope.size-2) {
        rope[idx+1] = adjustTail(rope[idx], rope[idx+1])
    }

    return rope
}

fun applyMotion(state: Pair<Position, Position>, direction: Direction): Pair<Position, Position> {
    val (headPos, tailPos) = state

    val newHeadPos = move(headPos, direction)

    val newTailPos = adjustTail(newHeadPos, tailPos)

    return Pair(newHeadPos, newTailPos)
}

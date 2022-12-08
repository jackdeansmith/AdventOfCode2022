import java.io.File
import kotlin.io.println
import kotlin.math.max

fun main(args: Array<String>) {
    val lines = File(args[0]).readLines()
    val trees = lines.map { line -> line.map { char ->
        char.toString().toInt()
    }}

    val visibilities = visibilities(trees)
    val ans = visibilities.flatten()
                          .filter { it }
                          .count()
    println("Part 1: $ans")

    println("Part 2: ${maxSenicScore(trees)}")
}

fun visibleTrees(treeHeights: List<Int>): List<Boolean> {
    var maxHeight = -1
    var result = mutableListOf<Boolean>()

    for (treeHeight in treeHeights) {
        result.add(treeHeight > maxHeight)
        maxHeight = max(maxHeight, treeHeight)
    }

    return result
}

fun List<List<Int>>.row(idx: Int): List<Int> {
    return this[idx]
}

fun List<List<Int>>.col(idx: Int): List<Int> {
    return this.map { it[idx] }
}

fun visibilities(trees: List<List<Int>>): List<List<Boolean>> {
    val forestSize = trees.size

    var result: MutableList<MutableList<Boolean>> = mutableListOf()
    repeat(forestSize) {
        var newRow = mutableListOf<Boolean>()
        repeat(forestSize) {
            newRow.add(false)
        }
        result.add(newRow)
    }

    // Left
    for(r in 0..forestSize-1) {
        val heights = trees.row(r)
        val rowVisibilities = visibleTrees(heights)

        for(c in 0..forestSize-1) {
            result[r][c] = result[r][c] || rowVisibilities[c]
        }
    }

    // Right 
    for(r in 0..forestSize-1) {
        val heights = trees.row(r)
        val rowVisibilities = visibleTrees(heights.reversed()).reversed()

        for(c in 0..forestSize-1) {
            result[r][c] = result[r][c] || rowVisibilities[c]
        }
    }

    // Down
    for(c in 0..forestSize-1) {
        val heights = trees.col(c)
        val colVisibilities = visibleTrees(heights)

        for(r in 0..forestSize-1) {
            result[r][c] = result[r][c] || colVisibilities[r]
        }
    }

    // Up
    for(c in 0..forestSize-1) {
        val heights = trees.col(c)
        val colVisibilities = visibleTrees(heights.reversed()).reversed()

        for(r in 0..forestSize-1) {
            result[r][c] = result[r][c] || colVisibilities[r]
        }
    }

    return result
}

fun senicScoreForSightline(height: Int, sightline: List<Int>): Int {
    var viewingDistance = 0

    for(tree in sightline) {
        viewingDistance += 1
        if (tree >= height){
            break
        }
    }

    return viewingDistance
}

fun senicScore(trees: List<List<Int>>, row: Int, col: Int): Int {
    val forestSize = trees.size

    val height = trees[row][col]

    var rightSightline: MutableList<Int> = mutableListOf()
    for (c in col+1..forestSize-1) {
        rightSightline.add(trees[row][c])
    }

    var leftSightline: MutableList<Int> = mutableListOf()
    for (c in col-1 downTo 0) {
        leftSightline.add(trees[row][c])
    }

    var downSightline: MutableList<Int> = mutableListOf()
    for (r in row+1..forestSize-1) {
        downSightline.add(trees[r][col])
    }

    var upSightline: MutableList<Int> = mutableListOf()
    for (r in row-1 downTo 0) {
        upSightline.add(trees[r][col])
    }

    // println("Scores: ${senicScoreForSightline(height, rightSightline)}, ${senicScoreForSightline(height, leftSightline)}, ${senicScoreForSightline(height, upSightline)}, ${senicScoreForSightline(height, downSightline)}")

    return senicScoreForSightline(height, rightSightline) * 
            senicScoreForSightline(height, leftSightline) * 
            senicScoreForSightline(height, upSightline) * 
            senicScoreForSightline(height, downSightline)
}

fun maxSenicScore(trees: List<List<Int>>): Int {
    val forestSize = trees.size

    var maxScore = 0

    for(r in 0..forestSize-1) {
        for(c in 0..forestSize-1) {
            val score = senicScore(trees, r, c)
            maxScore = max(maxScore, score)
        }
    }

    return maxScore
}
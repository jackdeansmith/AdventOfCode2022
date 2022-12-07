import java.io.File
import kotlin.io.println

fun main(args: Array<String>) {
    val input = File(args[0]).readText()

    val fs = FS()
    val commands = input.split("$")
                    .filter { it.length > 0 }
                    .map { it.split("\n")}
                    .map { it.map {line -> line.trim()}}
                    .map { it.filter {line -> line.length > 0}}
    for(command in commands) {
        val cmd = command[0] 
        val parts = cmd.split(" ")

        when(parts[0]) {
            "cd" -> {
                val destination = parts[1]
                when(destination) {
                    "/" -> fs.cdRoot()
                    ".." -> fs.cdUp()
                    else -> fs.cdTo(parts[1])
                }
            }
            "ls" -> {
                for(line in command.slice(1..command.size-1)) {
                    // println(line)

                    val parts2 = line.split(" ")
                    when(parts2[0]) {
                        "dir" -> fs.addDir(parts2[1])
                        else -> {
                            fs.addFile(parts2[1], parts2[0].toInt())
                        }
                    }
                }
            }
        }
    }

    // fs.root.printTree()

    val allSizes = allDirSizes(fs.root)

    val ans1 = allSizes
                .filter {it <= 100000}
                .sum()

    println(ans1)

    val totalSpace = 70000000
    val spaceNeeded = 30000000
    val spaceUsed = allSizes[0]
    val spaceRemaining = totalSpace - spaceUsed
    val deleteAtLeast = spaceNeeded - spaceRemaining

    val ans2 = allSizes.filter { it >= deleteAtLeast}
                       .sorted()
                       .first()

    println(ans2)
}

data class FNode(val name: String, val size: Int)

class DNode(var name: String, var files: MutableList<FNode>, var subdirs: MutableList<DNode>, val parent: DNode?)

fun DNode.printTree(indentLevel: Int=0) {
    val spacing = " ".repeat(indentLevel * 4)
    for(file in this.files) {
        println("$spacing- ${file.name} (file, size=${file.size})")
    }
    for(dir in this.subdirs) {
        println("$spacing- ${dir.name} (dir)")
        dir.printTree(indentLevel+1)
    }
}

class FS() {
    public val root: DNode = DNode("/", mutableListOf(), mutableListOf(), null)

    private var currentDirectory: DNode = root

    fun cdTo(name: String) {
        currentDirectory = currentDirectory.subdirs.find { it.name == name}!!
    }

    fun cdUp() {
        currentDirectory = currentDirectory.parent!!
    }

    fun cdRoot() {
        currentDirectory = root
    }

    fun addDir(name: String) {
        if(currentDirectory.subdirs.filter {it.name == name}.count() == 0){
            val newDir = DNode(name, mutableListOf(), mutableListOf(), currentDirectory)
            currentDirectory.subdirs.add(newDir)
        }
    }

    fun addFile(name: String, size: Int) {
        if(currentDirectory.files.filter {it.name == name}.count() == 0){
            val newFile = FNode(name, size)
            currentDirectory.files.add(newFile)
        }
    }
}

fun allDirSizes(node: DNode): MutableList<Int> {
    var res: MutableList<Int> = mutableListOf()

    var sizeOfFiles = node.files.map {it.size}.sum()

    var sizeOfDirs = 0
    for(subdir in node.subdirs) {
        val subDirSizes = allDirSizes(subdir)
        sizeOfDirs += subDirSizes[0]
        res.addAll(subDirSizes)
    }

    res.add(0, sizeOfDirs + sizeOfFiles)

    return res
}
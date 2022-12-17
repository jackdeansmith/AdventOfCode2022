import java.io.File
import kotlin.io.println
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main(args: Array<String>) {
    val parsedValves = File(args[0]).readLines().map { parseline(it) }
    val valves = parsedValves.withIndex().map { (idx, v) -> Valve(idx, v.name, v.flowrate, v.tunnels.map { tunnelName -> parsedValves.indexOfFirst { it.name == tunnelName } }) }

    val costs = travelCosts(valves)

    val startNode = valves.indexOfFirst { it.name == "AA" }

    val usableValves = valves.filter{ it.flowrate > 0}
                             .map { it.idx }.toSet()

    // val ans1 = maxUtility(startNode, valves, usableValves, costs, 30)
    // println("ans1: $ans1")

    // println(nextStates(AgentLocation(startNode, 0), usableValves, costs, false))

    // val timeToTeach = 4
    // Next agent 1 state: AgentLocation(destination=1, timeLeft=1), next agent 2 state: AgentLocation(destination=3, timeLeft=1)
    val ans2 = maxUtilityMultiplayer(AgentLocation(0, 0), AgentLocation(0, 0), valves, costs, usableValves, 6)
    // val ans2 = maxUtilityMultiplayer(AgentLocation(startNode, 0), AgentLocation(startNode, 0), valves, costs, usableValves, 28)
    println("ans2: $ans2")
}

data class AgentLocation(val destination: Int, val timeLeft: Int)

// return the list of possible agent location after a single time step, if agent location is in transit, then it will get one timestep closer to the destination
// if it's at a destination, then it will begin transit to another unopened valve with a timeLeft defined by the cost of transit
fun nextStates(agent: AgentLocation, unopenedValves:Set<Int>, costs: List<List<Int>>, agentPulledValve: Boolean): List<AgentLocation> {
    val nextSpots = mutableListOf<AgentLocation>()

    // If the agent pulled the valve, it has to wait in place for the next time step
    if(agentPulledValve){
        return listOf(agent)
    }

    if(agent.timeLeft == 0) {
        for (v in unopenedValves) {
            if(v == agent.destination) continue
            nextSpots.add(AgentLocation(v, costs[agent.destination][v]))
        }
    } else {
        nextSpots.add(AgentLocation(agent.destination, agent.timeLeft - 1))
    }
    return nextSpots
}

fun maxUtilityMultiplayer(agent1: AgentLocation, agent2: AgentLocation, valves: List<Valve>, costs: List<List<Int>>, unopenedValves:Set<Int>, timeBudget: Int, print:Boolean=true): Int {

    if(timeBudget <= 1) {
        return 0
    }

    var remainingValves = unopenedValves
    var currentUtil = 0
    var remainingUtil = 0

    var agent1PulledValve = false
    if(agent1.timeLeft == 0){
        val node = agent1.destination
        if(node in unopenedValves && valves[node].flowrate > 0) {
            agent1PulledValve = true
            currentUtil += valves[node].flowrate * (timeBudget - 1)
            remainingValves = (remainingValves - node)
        }
    }

    var agent2PulledValve = false
    if(agent2.timeLeft == 0){
        val node = agent2.destination
        if(node in remainingValves && valves[node].flowrate > 0) {
            agent2PulledValve = true
            currentUtil += valves[node].flowrate * (timeBudget - 1)
            remainingValves = (remainingValves - node)
        }
    }

    for(nextAgent1State in nextStates(agent1, remainingValves, costs, agent1PulledValve)) {
        for(nextAgent2State in nextStates(agent2, remainingValves, costs, agent2PulledValve)) {

            // TODO: prune shooting off down tunnels that are too long
            val candidateUtil = maxUtilityMultiplayer(nextAgent1State, nextAgent2State, valves, costs, remainingValves, timeBudget - 1, false)
            remainingUtil = max(remainingUtil, candidateUtil)

            if(print){
                println("Next agent 1 state: $nextAgent1State, next agent 2 state: $nextAgent2State")
                println("Found residual util: $candidateUtil")
            }
        }
    }
    if(print){
        println()
    }

    return currentUtil + remainingUtil
} 

fun maxUtility(currentNode: Int, valves: List<Valve>, unopenedValves:Set<Int>, costs: List<List<Int>>, timeBudget: Int): Int {
    //Base case: no more time left, takes one second to open a valve so one sec is no time left
    if(timeBudget <= 1) {
        return 0
    }

    var remainingBudget = timeBudget
    var remainingValves = unopenedValves
    var currentUtil = 0
    if(valves[currentNode].flowrate > 0) {
        currentUtil = valves[currentNode].flowrate * (remainingBudget - 1)
        remainingBudget -= 1
        remainingValves = (remainingValves - currentNode)
    }

    val remainingUtil = if(remainingValves.size > 0) { remainingValves.map { maxUtility(it, valves, remainingValves, costs, remainingBudget - costs[currentNode][it]) }.max() } else {0}

    return currentUtil + remainingUtil
}

// Uses floyd warshall to compute the cost of traveling between all pairs of valves
fun travelCosts(valves: List<Valve>): List<List<Int>> {
    var costTable: MutableList<MutableList<Int>> = MutableList(valves.size) { MutableList(valves.size) { Int.MAX_VALUE } }
    for (i in 0 until valves.size) {
        costTable[i][i] = 0
    }

    for (v in valves) {
        for (t in v.tunnels) {
            costTable[v.idx][t] = 1
        }
    }

    for (k in 0 until valves.size) {
        for (i in 0 until valves.size) {
            for (j in 0 until valves.size) {
                // if costTable[i][k] or costTable[k][j] is Int.MAX_VALUE, then the cost of traveling from i to j is Int.MAX_VALUE
                if(costTable[i][k] == Int.MAX_VALUE || costTable[k][j] == Int.MAX_VALUE) {
                    continue
                }
                costTable[i][j] = min(costTable[i][j], costTable[i][k] + costTable[k][j])
            }
        }
    }

    return costTable
}

data class Valve(val idx: Int, val name: String, val flowrate: Int, val tunnels: List<Int>)

data class ParsedValve(val name: String, val flowrate: Int, val tunnels: List<String>)

// Parses a line formatted like
// "Valve AA has flow rate=0; tunnels lead to valves DD, II, BB"
// or like "Valve HH has flow rate=22; tunnel leads to valve GG"
fun parseline(line: String): ParsedValve {
    val regex = Regex("Valve (\\w+) has flow rate=(\\d+); tunnel(?:s?) lead(?:s?) to valve(?:s?) (\\w+(, \\w+)*)")
    val match = regex.matchEntire(line)
    if (match != null) {
        val (name, flowrate, tunnels) = match.destructured
        return ParsedValve(name, flowrate.toInt(), tunnels.split(", "))
    } else {
        throw IllegalArgumentException("Invalid line: $line")
    }
}

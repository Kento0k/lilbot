package ru.spbstu.competition.protocol.data

import ru.spbstu.competition.game.RiverState
import ru.spbstu.competition.game.State

class Search{

    fun heuristic(a: Int, b: Int, state: State): Double {
        return Math.abs(state.coordinates[a]!!.x!! - state.coordinates[b]!!.x!!) + Math.abs(state.coordinates[a]!!.y!! - state.coordinates[b]!!.y!!)
    }

    fun aStar(state: State, argSource: Int, argTarget: Int): MutableSet<River> {
        var foundedWay = mutableSetOf<River>()

        var INFINITE = 1000000.0

        var priorityQueue = mutableMapOf<Int, Double>()
        var cameFrom = mutableMapOf<Int, Int>()
        var wayCost = mutableListOf(INFINITE)

        priorityQueue.put(argSource, 0.0)

        while(priorityQueue.size != 0) {

            var current = null
            var minPriority = INFINITE
            var currSite: Int = argTarget
            for((x, y) in priorityQueue) {
                if(y < minPriority) {
                    minPriority = y
                    currSite = x
                }
            }

            if (currSite == argTarget) {
                return foundedWay
            }
            val neighborSites = state
                    .rivers
                    .entries
                    .filter { it.key.source == currSite || it.key.target == currSite }
                    .flatMap { listOf(it.key.source, it.key.target) }
                    .toSet()
            for (site in neighborSites) {
                if (site != currSite) {
                    var newCost = wayCost[currSite] + 1
                    if (newCost < wayCost[site]) {
                        wayCost[site] = newCost
                        var priority = newCost + heuristic(argTarget, site, state)
                        priorityQueue.put(site, priority)
                        cameFrom[site] = currSite
                    }
                }
            }

        }

        return foundedWay
    }

}
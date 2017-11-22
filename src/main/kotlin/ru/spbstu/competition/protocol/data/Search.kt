package ru.spbstu.competition.protocol.data

import ru.spbstu.competition.game.State

class Search {

    private fun heuristic(a: Int, b: Int, state: State): Double {
        return Math.abs(state.coordinates[a]!!.x!! - state.coordinates[b]!!.x!!) + Math.abs(state.coordinates[a]!!.y!! - state.coordinates[b]!!.y!!)
    }

    fun aStar(state: State, argSource: Int, argTarget: Int): MutableMap<Int, Int> {

        val INFINITE = 1000000.0

        val priorityQueue = mutableMapOf<Int, Double>()
        val foundedWay = mutableMapOf<Int, Int>()
        val wayCost = mutableListOf(INFINITE)

        priorityQueue.put(argSource, 0.0)

        while(priorityQueue.isNotEmpty()) {

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
                    val newCost = wayCost[currSite] + 1
                    if (newCost < wayCost[site]) {
                        wayCost[site] = newCost
                        val priority = newCost + heuristic(argTarget, site, state)
                        priorityQueue.put(site, priority)
                        foundedWay[site] = currSite
                    }
                }
            }

        }

        return foundedWay
    }

}
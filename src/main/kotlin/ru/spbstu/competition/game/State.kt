package ru.spbstu.competition.game

import ru.spbstu.competition.protocol.data.Claim
import ru.spbstu.competition.protocol.data.River
import ru.spbstu.competition.protocol.data.Setup

enum class RiverState{ Our, Enemy, Neutral }

class State {

    class Pairs(val x: Double?, val y: Double?)
//    class Component {
//        var sited = mutableSetOf<Int>()
//    }

    val rivers = mutableMapOf<River, RiverState>()
    var mines = listOf<Int>()
    var myId = -1

    var coordinates = mutableMapOf<Int, Pairs>()
    //var linkComponents = mutableSetOf<Component>()

    var currentWay = mutableSetOf<River>()

    fun init(setup: Setup) {
        myId = setup.punter
        for(river in setup.map.rivers) {
            rivers[river] = RiverState.Neutral
        }
        for((id, x, y) in setup.map.sites) {
            coordinates[id] = Pairs(x, y)
        }
        for(mine in setup.map.mines) {
            mines += mine
        }
        //findComponents(setup.map.sites.size)
    }

//    private fun depthSearch(argSite: Int, argComponent: Component, used: Array<Int?>) {
//        used[argSite] = 1
//        argComponent.sited.add(argSite)
//        val neighborRivers = rivers
//                .entries
//                .filter { it.key.source == argSite || it.key.target == argSite }
//                .flatMap { listOf(it.key.source, it.key.target) }
//                .toSet()
//        for(site in neighborRivers) {
//            if(site != argSite) {
//                if(used[site] == null)
//                    depthSearch(site, argComponent, used)
//            }
//        }
//    }
//
//    private fun findComponents(size: Int) {
//        val used = arrayOfNulls<Int>(size)
//        for((index, value) in used.withIndex()) {
//            if(value == null) {
//                val newComponent = Component()
//                depthSearch(index, newComponent, used)
//                linkComponents.add(newComponent)
//            }
//        }
//    }

    fun update(claim: Claim) {
        rivers[River(claim.source, claim.target)] = when(claim.punter) {
            myId -> RiverState.Our
            else -> RiverState.Enemy
        }
    }
}

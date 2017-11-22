package ru.spbstu.competition.game

import ru.spbstu.competition.protocol.data.Claim
import ru.spbstu.competition.protocol.data.River
import ru.spbstu.competition.protocol.data.Setup

enum class RiverState{ Our, Enemy, Neutral }

class State {
    class Pairs(val x: Double?, val y: Double?)
    class Component() {
        var sited = mutableSetOf<Int>()
        fun addSite(argSite: Int) {
            sited.add(argSite)
        }
    }
    val rivers = mutableMapOf<River, RiverState>()
    var mines = listOf<Int>()
    var myId = -1
    var coordinates = mutableMapOf<Int, Pairs>()
    var linkComponents = mutableSetOf<Component>()

    fun init(setup: Setup) {
        myId = setup.punter
        for((id, x, y) in setup.map.sites) {
            coordinates[id] = Pairs(x, y)
        }
        for(river in setup.map.rivers) {
            rivers[river] = RiverState.Neutral

        }
        for(mine in setup.map.mines) {
            mines += mine
        }
    }

    fun update(claim: Claim) {
        rivers[River(claim.source, claim.target)] = when(claim.punter) {
            myId -> RiverState.Our
            else -> RiverState.Enemy
        }
    }
}

package ru.spbstu.competition.game

import ru.spbstu.competition.protocol.Protocol
import ru.spbstu.competition.protocol.data.Search

class Intellect(val state: State, val protocol: Protocol) {

    fun makeMove() {

        if(state.currentWay.isEmpty()) {

            val mines = state.mines
            val used = mutableSetOf<Int>()
            var minWay = 1000000
            var doWay = mutableMapOf<Int, Int>()
            for(currentMine in mines) {
                used.add(currentMine)
                for(lookMine in mines) {
                    if(used.contains(lookMine))
                        continue;
                    val search = Search()
                    val way = search.aStar(state, currentMine, lookMine)
                    if(way.size != 0 && way.size < minWay) {
                        minWay = way.size
                        doWay = way
                    }
                }
            }
            if (doWay.size != 0) {
                for (oneWay in doWay) {
                    val try0 = state.rivers.entries.find { (river, riverState) ->
                        riverState == RiverState.Neutral && (river.source in state.mines || river.target in state.mines)
                        && (oneWay.key == river.source || oneWay.key == river.target
                                || oneWay.value == river.source || oneWay.value == river.target)
                    }
                    if (try0 != null) {
                        doWay.remove(oneWay.key)
                        return protocol.claimMove(try0.key.source, try0.key.target)
                    }
                }
            }
            else {

                val try1 = state.rivers.entries.find { (river, riverState) ->
                    riverState == RiverState.Neutral && (river.source in state.mines || river.target in state.mines)
                }
                if(try1 != null) return protocol.claimMove(try1.key.source, try1.key.target)

                val ourSites = state
                        .rivers
                        .entries
                        .filter { it.value == RiverState.Our }
                        .flatMap { listOf(it.key.source, it.key.target) }
                        .toSet()

                // If there is a river between two our pointsees, take it!
                val try2 = state.rivers.entries.find { (river, riverState) ->
                    riverState == RiverState.Neutral && (river.source in ourSites && river.target in ourSites)
                }
                if(try2 != null) return protocol.claimMove(try2.key.source, try2.key.target)

                // If there is a river near our pointsee, take it!
                val try3 = state.rivers.entries.find { (river, riverState) ->
                    riverState == RiverState.Neutral && (river.source in ourSites || river.target in ourSites)
                }
                if(try3 != null) return protocol.claimMove(try3.key.source, try3.key.target)

                // Bah, take anything left
                val try4 = state.rivers.entries.find { (_, riverState) ->
                    riverState == RiverState.Neutral
                }
                if (try4 != null) return protocol.claimMove(try4.key.source, try4.key.target)


            }

        }
        else {

            for (oneWay in state.currentWay) {
                val try5 = state.rivers.entries.find { (river, riverState) ->
                    riverState == RiverState.Neutral && (river.source in state.mines || river.target in state.mines)
                            && (oneWay.key == river.source || oneWay.key == river.target
                            || oneWay.value == river.source || oneWay.value == river.target)
                }
                if (try5 != null) {
                    state.currentWay.remove(oneWay.key)
                    return protocol.claimMove(try5.key.source, try5.key.target)
                }
                val ourSites = state
                        .rivers
                        .entries
                        .filter { it.value == RiverState.Our }
                        .flatMap { listOf(it.key.source, it.key.target) }
                        .toSet()
                val try6 = state.rivers.entries.find { (river, riverState) ->
                    riverState == RiverState.Neutral && (river.source in ourSites && river.target in ourSites)
                            && (oneWay.key == river.source || oneWay.key == river.target
                            || oneWay.value == river.source || oneWay.value == river.target)
                }
                if (try6 != null) {
                    state.currentWay.remove(oneWay.key)
                    return protocol.claimMove(try6.key.source, try6.key.target)
                }
            }

        }
        protocol.passMove()

    }

}

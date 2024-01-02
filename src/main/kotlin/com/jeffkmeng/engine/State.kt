package com.jeffkmeng.engine

import com.jeffkmeng.UIState
import com.jeffkmeng.User
import kotlin.random.Random

abstract class BaseEvent {
    abstract fun update(state: State)
}

class IllegalEventException(message:String): Exception(message)


/**
 * All the information for the current game state
 */
class State(val players: List<Player>, val deck: List<Character>, startingPlayer: Player = players.random()) {

    var turn: Int = 1 // the turn number of the current turn, starting from 1
    var turnPlayer: Player = startingPlayer // the player whose turn it is
    var status: Status = Status.SELECT_ACTION

    var waitingOn: MutableSet<Player> = mutableSetOf(turnPlayer)
    var currentAction: Action? = null

    /**
     * Returns the UI state for a given player.
     */
    // TODO: "ForPlayer" feels redundant since it's already in the signature of the function, but it also feels somewhat
    // useful so I'm not sure how to reconcile that
    fun getUIState(player: Player): UIState {
        return UIState()
    }
}

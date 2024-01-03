package com.jeffkmeng.engine

import com.jeffkmeng.UIState



/**
 * All the information for the current game state
 */
class State(val players: List<Player>, val deck: List<Character>, startingPlayer: Player = players.random()) {

    var turn: Int = 1 // the turn number of the current turn, starting from 1
    var currentTurnPlayer: Player =
        startingPlayer // the player whose turn it is (which is not necessarily the player we're waiting on)
    var status: Status = Status.SELECT_ACTION

    var waitingOn: MutableSet<Player> = mutableSetOf(currentTurnPlayer)
    var currentAction: Action? = null

    /**
     * Returns the UI state for a given player.
     */
    fun getUIState(player: Player): UIState {
        return UIState(player.cards)
    }
}

/**
 * TODO: To represent the list of challenge responses, etc., it might be best to split State into one State for each status.
 * Then we can also implement .nextStatus() => State to return the State subclass with the next Status
 */
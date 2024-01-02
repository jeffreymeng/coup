package com.jeffkmeng.engine

import com.jeffkmeng.*

abstract class Engine(
    val users: List<User>,
    /**
     * Which characters are included in this game variant
     */
    val characters: List<Character>,
    /**
     * Any actions that aren't specific to a character (ex. Income, Coup)
     */
    val baseActions: List<ActionManifest>
) {
    val actions: List<ActionManifest>
        get() = baseActions + characters.flatMap { it.actions }


    /**
     * The status represents the current phase of the turn (e.g. who are we waiting on?). Each possible status is a
     * point in the game where a _player_ most make a decision or action.
     */

    val state: State

    init {
        // TODO: how do you design this in a way that can be overwritten? e.g. if you only wanted to deal one card to each person
        val cards = characters.flatMap { character -> MutableList(3) { character } }.shuffled().iterator()
        val players = users.map { Player(it, List(2) { cards.next() }) }
        val deck = cards.asSequence().toList()
        state = State(players, deck)
    }


    fun receiveEvent(event: BaseEvent) {
        event.update(state)
        incrementStatus()
    }

    fun getUIState(user: User): UIState {
        val player =
            state.players.find { it.user == user } ?: throw Exception("Couldn't find player corresponding to $user")
        return state.getUIState(player)
    }

    fun incrementStatus() {
        state.status = when (state.status) {
            Status.SELECT_ACTION -> Status.CHALLENGE
            Status.CHALLENGE -> Status.BLOCK
            Status.BLOCK -> Status.CHALLENGE_BLOCK
            Status.CHALLENGE_BLOCK -> Status.RESOLVE
            Status.RESOLVE -> {
                // TODO reset turn
                Status.SELECT_ACTION
            }
        }
        val canSkipStatus = when (state.status) {
            Status.CHALLENGE -> !state.currentAction!!.canBeChallenged
            Status.BLOCK -> !state.currentAction!!.canBeBlocked
            Status.RESOLVE -> state.currentAction!!.getResolveWaitingOn().isEmpty()
            else -> false
        }

        state.waitingOn = when(state.status) {
            Status.SELECT_ACTION -> mutableSetOf(state.turnPlayer)
            Status.CHALLENGE -> state.players.toMutableSet()
            Status.BLOCK -> if (state.currentAction is TargetedAction) mutableSetOf((state.currentAction as TargetedAction).target) else mutableSetOf()
            Status.RESOLVE -> state.currentAction!!.getResolveWaitingOn()
        }

        if (canSkipStatus) {
            if (state.status == Status.RESOLVE) {
                state.currentAction!!.resolve(state, null)
            }
            incrementStatus()
        }
    }


    // todo: things like executeAction(), etc
    // todo: maybe a debug print formatter?
}

package com.jeffkmeng.engine

import com.jeffkmeng.*


open class Engine(
    val users: List<User>,
    /**
     * The deck containing the initial characters of the variant.
     * Note that the initialDeck is expected to be pre-shuffled.
     */
    initialDeck: List<Character>,
    /**
     * Any actions that aren't specific to a character (ex. Income, Coup)
     */
    baseActions: List<ActionManifest>
) {
    val actions: Set<ActionManifest> = (baseActions + initialDeck.flatMap { it.actions }).toSet()


    /**
     * The status represents the current phase of the turn (e.g. who are we waiting on?). Each possible status is a
     * point in the game where a _player_ most make a decision or action.
     */

    var state: State

    init {
        // TODO: how do you design this in a way that can be overwritten? e.g. if you only wanted to deal one card to each person
        // ig for now you can just overwrite the state after construction
        val cards = initialDeck.iterator()
        val players = users.map { Player(it, List(2) { cards.next() }) }
        val deck = cards.asSequence().toList()
        state = SelectActionState(players, deck, 1, players.random())
    }


    fun receiveMessage(message: Message) {
        state = state.receiveMessage(message)
    }

    fun getPlayer(user: User) =
        state.players.find { it.user == user } ?: throw Exception("Couldn't find player corresponding to $user")

    fun getUIState(user: User): UIState {
        val player = getPlayer(user)
        return state.getUIState(player)
    }

//    protected fun incrementStatus() {
//        state.status = when (state.status) {
//            Status.SELECT_ACTION -> Status.CHALLENGE
//            Status.CHALLENGE -> Status.BLOCK
//            Status.BLOCK -> Status.CHALLENGE_BLOCK
//            Status.CHALLENGE_BLOCK -> Status.RESOLVE
//            Status.RESOLVE -> {
//                state.turn++
//                state.currentTurnPlayer =
//                    state.players.let { it.listIterator(it.indexOf(state.currentTurnPlayer)).nextOr(it.first()) }
//                Status.SELECT_ACTION
//            }
//        }
//
//        val canSkipStatus = when (state.status) {
//            // TODO: is there a way to get the compiler to verify that when the status is not SELECT_ACTION the currentAction is not null?
//            // Maybe refactor to create a new State type for each status (e.g. SelectActionState, ChallengeState, etc)
//            Status.CHALLENGE -> !state.currentAction!!.canBeChallenged
//            Status.BLOCK -> !state.currentAction!!.canBeBlocked
//            Status.RESOLVE -> state.currentAction!!.getResolveWaitingOn().isEmpty()
//            else -> false
//        }
//
//        state.waitingOn = when (state.status) {
//            Status.SELECT_ACTION -> mutableSetOf(state.currentTurnPlayer)
//            Status.CHALLENGE -> state.players.toMutableSet()
//            Status.BLOCK -> state.currentAction.let { if (it is TargetedAction) mutableSetOf(it.target) else state.players.toMutableSet() }
//            Status.CHALLENGE_BLOCK -> mutableSetOf(state.currentAction!!.actor)
//            Status.RESOLVE -> state.currentAction!!.getResolveWaitingOn().toMutableSet()
//        }
//
//        if (canSkipStatus) {
//            if (state.status == Status.BLOCK) {
//                // if we skip the block phase, we must also skip the challenge block phase (e.g. we must skip twice, once
//                // here and once during incrementStatus())
//                state.status = Status.CHALLENGE_BLOCK
//            } else if (state.status == Status.RESOLVE) {
//                // resolve the action before moving on
//                state.currentAction!!.resolve(state, null)
//            }
//            incrementStatus()
//        }
//    }


    // todo: things like executeAction(), etc
    // todo: maybe a debug print formatter?
}

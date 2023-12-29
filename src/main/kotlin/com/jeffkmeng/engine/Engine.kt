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

    val players: List<Player>
    val deck: List<Character>

    init {
        // TODO: how do you design this in a way that can be overwritten? e.g. if you only wanted to deal one card to each person
        val cards = characters.flatMap { character -> MutableList(3) { character } }.shuffled().iterator()
        players = users.map { Player(it, List(2) { cards.next() }) }
        deck = cards.asSequence().toList()
    }

    val state: State = State(players, deck)


    fun getUIStateForUser(user: User): UIState {
        val player = players.find { it.user == user } ?: throw Exception("Couldn't find player corresponding to $user")
        return state.getUIStateForPlayer(player)
    }


    // todo: things like executeAction(), etc
    // todo: maybe a debug print formatter?
}

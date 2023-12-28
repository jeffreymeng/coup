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
    val actions: List<ActionManifest> = baseActions + characters.flatMap{it.actions}

    val players: List<Player> = users.map{ Player(it, listOf()) }

    val state: State = State(players)

    fun getUIStateForUser(user: User): UIState {
        val player = players.find { it.user == user }
        if (player == null) {
            throw Exception("Couldn't find player corresponding to $user")
        }
        return state.getUIStateForPlayer(player)
    }

    // todo: things like executeAction(), etc
    // todo: maybe a debug print formatter?
}

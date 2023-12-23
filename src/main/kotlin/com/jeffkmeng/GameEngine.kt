package com.jeffkmeng

import kotlin.reflect.KClass

abstract class GameEngine(
    val users: List<User>,
    /**
     * Which characters are included in this game variant
     */
    val characters: List<Character>,
    /**
     * Any actions that aren't specific to a character (ex. Income, Coup)
     */
    val baseActions: List<Action>
) {
    val actions: List<Action> = baseActions + characters.flatMap{it.actions}

    val players: List<Player> = users.map{Player(it, listOf())}

    val state: GameState = GameState(players)

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

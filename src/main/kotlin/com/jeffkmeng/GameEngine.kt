package com.jeffkmeng

import kotlin.reflect.KClass

abstract class GameEngine(val players: List<Player>) {
    /**
     * Which characters are included in this game variant
     */
    abstract val characters: List<Character>

    /**
     * Any actions that aren't specific to a character (ex. Income, Coup)
     */
    abstract val baseActions: List<Action>


    val actions: List<Action> = baseActions + characters.flatMap{it.actions}

    val state: GameState = GameState(players)

    // todo: things like executeAction(), etc
}

// class BaseGameEngine(players: List<Player>) : GameEngine(players) {
// }

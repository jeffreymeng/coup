package com.jeffkmeng.engine

import com.jeffkmeng.User

/**
 * Information for a player for the actual game state.
 * Includes things like how much money they have, etc
 */
data class Player(val user: User, var cards: List<Character>) {
    var coins = 2
    val liveCards: List<Character>
        get() = cards.filter { it.isAlive }
}

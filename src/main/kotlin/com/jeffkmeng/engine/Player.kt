package com.jeffkmeng.engine

import com.jeffkmeng.User

/**
 * Information for a player for the actual game state.
 * Includes things like how much money they have, etc
 */
class Player(val user: User, var cards: List<Character>) {
    var coins = 2
    val liveCards: List<Character>
        get() = cards.filter { it.isAlive }

    // TODO: Note: with the current implementation, this can't be a data class since we need equality of players
    // to be referential, otherwise it wouldn't make sense to update the cards value

    override fun toString(): String {
        return "Player(id = $user.id)"
    }
}

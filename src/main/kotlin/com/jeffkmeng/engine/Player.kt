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


    override fun toString(): String {
        return "Player(uid = ${user.id})"
    }

    override fun equals(other: Any?): Boolean {
        return other is Player && this.user == other.user
    }

    override fun hashCode(): Int {
        return this.user.hashCode()
    }
}

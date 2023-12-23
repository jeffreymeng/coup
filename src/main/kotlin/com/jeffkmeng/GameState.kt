package com.jeffkmeng

/**
 * Information for a player for the actual game state.
 * Includes things like how much money they have, etc
 */
data class Player(val user: User, var cards: List<Character>) {
    var coins = 2

}

/**
 * Ex. Tax, Income, Foreign Aid, Coup, etc
 */
abstract class Action {
    abstract val id: String;

    /**
     * Is the given player allowed to execute this action? ie. do they have enough money?
     */
    abstract fun isLegal(gameState: GameState, player: Player): Boolean

    /**
     * If the given player executed the action and was challenged, do they lose a life?
     */
    fun isLegitimate(state: GameState, player: Player): Boolean =
        player.cards.any{it.isAlive && this in it.actions}

    abstract fun resolve(state: GameState)
    // TODO: should this return a new GameState or just mutate the existing one?
    // returning a new one might be annoying with having to copy all the players, so mutating might
    // be easier?
}

/**
 * One character in Coup, like Captain or Duke
 */
abstract class Character {
    companion object {
        abstract fun doSomething()
    }
    /**
     * What actions can this character execute?
     * Ex. Duke can execute Tax
     */
    abstract val actions: Array<Action>

    /**
     * What actions can this character block?
     * Ex. Duke can block Foreign Aid
     */
    abstract val blockedActions: Array<Action>

    var isAlive = true
}

/**
 * All the information for the current game state
 */
data class GameState(val players: List<Player>) {
    /**
     * Returns the UI state for a given player.
     */
    fun getUIStateForPlayer(player: Player): UIState {
        return UIState()
    }
}

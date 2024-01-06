package com.jeffkmeng

import com.jeffkmeng.engine.Character
import com.jeffkmeng.engine.Player

/**
 * Information for the actual end-user that is playing the game
 *
 * @param id The id for the user, which must be unique across all users in the game
 * @param name A name for the user
 */
data class User(val id: String, val name: String) {
    override fun toString() = "User($name)"
    override fun equals(other: Any?): Boolean {
        return other is User && other.id == id
    }

    override fun hashCode(): Int {
        return this.id.hashCode()
    }
}

abstract class UICharacter(val isAlive: Boolean)
class UIVisibleCharacter(val character: Character) : UICharacter(character.isAlive) {
    override fun equals(other: Any?): Boolean =
        other is UIVisibleCharacter && other.isAlive == isAlive && other.character == character

    override fun toString() = "UIVisibleCharacter(${if (isAlive) "alive" else "not alive"}, Character(${character.id}))"
}

class UIHiddenCharacter(isAlive: Boolean) : UICharacter(isAlive) {
    companion object {
        fun from(character: Character) = UIHiddenCharacter(character.isAlive)
    }

    override fun equals(other: Any?): Boolean = other is UIHiddenCharacter && other.isAlive == isAlive

    override fun toString() = "UIHiddenCharacter(${if (isAlive) "alive" else "not alive"})"
}

data class UIPlayer(val user: User, val cards: List<UICharacter>) {
    override fun toString() = "UIPlayer(${user.name}, ${cards.map { it.toString() }})"
}


/**
 * All the information necessary for one client to render the game
 * for one particular player. No sensitive information can be exposed here.
 *
 */
class UIState(val forUser: User, val players: List<UIPlayer>) {
    val cards: List<UIVisibleCharacter>
        get() {
            val cards = players.find { it.user == forUser }!!.cards
            assert(cards.all { it is UIVisibleCharacter }) { "Expected cards for UIState `forUser` to be visible!" }
            return cards as List<UIVisibleCharacter>
        }

    /**
     * Creates a UIState from a list of Players and a reference to the current player.
     * @param forPlayer The player this UI state is for (e.g. they will be able to see their cards, but not others' cards)
     * @param players The list of all players in the game.
     */
    constructor(forPlayer: Player, players: List<Player>) : this(
        forPlayer.user,
        players.map { player ->
            UIPlayer(
                player.user,
                player.cards.map { if (player == forPlayer) UIVisibleCharacter(it) else UIHiddenCharacter.from(it) }
            )
        }
    ) {
        assert(forPlayer in players)
    }


    // idk. also note that this interface will be duplicated here and again in TypeScript
    // and everything here needs to be serializable. Maybe protobuf?

    // currentPlayer: Player
    // status: Status

    // // TODO: is there a better way to display the available things a player can do? e.g. a list of actions, or they can block, etc.
    // availableActions = List<Action>? // this would be null UNLESS currentPlayer == thisPlayer && status == pickAction
    // // we probably also need something for blocking and challenging?

    // TODO: make sure the ID of a character is not exposed to the user
    override fun equals(other: Any?): Boolean {
        return other is UIState && other.forUser == forUser && other.players == players
    }

    override fun toString() = "UIState($forUser, ${players.map { it.toString() }})"
}
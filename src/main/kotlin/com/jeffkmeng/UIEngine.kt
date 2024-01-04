package com.jeffkmeng

import com.jeffkmeng.engine.Character

/**
 * Information for the actual end-user that is playing the game
 */
data class User(val id: String, val name: String) {

}

abstract class UICharacter(val isAlive: Boolean)
class UIVisibleCharacter(isAlive: Boolean, val character: Character) : UICharacter(isAlive)
class UIHiddenCharacter(isAlive: Boolean) : UICharacter(isAlive)

class UIPlayer(val user: User, val cards: List<UICharacter>)

/**
 * All the information necessary for one client to render the game
 * for one particular player. No sensitive information can be exposed here.
 */
class UIState(val cards: List<Character>/* val players: List<UIPlayer> */) {
    // idk. also note that this interface will be duplicated here and again in TypeScript
    // and everything here needs to be serializable. Maybe protobuf?

    // currentPlayer: Player
    // status: Status

    // // TODO: is there a better way to display the available things a player can do? e.g. a list of actions, or they can block, etc.
    // availableActions = List<Action>? // this would be null UNLESS currentPlayer == thisPlayer && status == pickAction
    // // we probably also need something for blocking and challenging?

    // TODO: make sure the ID of a character is not exposed to the user
    override fun equals(other: Any?): Boolean {
        return other is UIState && other.cards == this.cards
    }
}

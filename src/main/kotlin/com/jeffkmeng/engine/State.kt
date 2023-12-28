package com.jeffkmeng.engine

import com.jeffkmeng.UIState
import com.jeffkmeng.User





/**
 * All the information for the current game state
 */
data class State(val players: List<Player>) {
    /**
     * Status of the current turn (e.g. what are we waiting on?)
     */
    enum class Status {
        // TODO: technically, challenge, block, and resolve are separate stages of a turn
        // however, users should be presented with the option to do all three, if applicable
        // e.g. if a player is the target of an assassination, they should see options to
        // do: [challenge] [block as contessa] [reveal Duke] [reveal Ambassador]
        // instead of presenting these linearly. This should probably be treated as a "premove" and can be handled by
        // either the client or the backend
        // e.g. Selecting [reveal Duke] implies no challenge => no block => reveal Duke by this player,
        // but other users could still potentially challenge if they want to. Selecting [reveal Duke] does not
        // actually reveal the duke until all other players have decided not to challenge (see below)
        // If another player successfully challenges, then nothing is revealed
        // If another player fails, then the duke is revealed

        // Also, we need to somehow decide how to handle challenges
        // it probably wouldn't be best to make every player "approve" every action, as in *most*
        // cases a player won't want to challenge an action if they're not a target of it (assume duke targets
        // everybody, not nobody). However, they should have the option to do so, so maybe the best way to handle
        // this is to have actions be approved by default after a 5s delay by everybody except the target of
        // an action, who must manually approve or challenge an action.

        // TODO: do we want a delay before revealing if a challenge is successful to add suspense? (I think yes)

        SELECT_ACTION, // the current player is choosing which action to perform
        CHALLENGE, // players are deciding whether to challenge the action
        BLOCK, // the targeted player(s) are deciding whether to block the action (note: if you block you implicitly
            // accept the initial claim and give up the right to challenge)

        CHALLENGE_BLOCK, // players are deciding whether to challenge the block
        RESOLVE // the target is deciding how to resolve the action (e.g. choosing a card, etc.)
    }

    var status: Status = Status.SELECT_ACTION

    /**
     * Returns the UI state for a given player.
     */
    fun getUIStateForPlayer(player: Player): UIState {
        return UIState()
    }
}

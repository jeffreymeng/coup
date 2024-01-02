package com.jeffkmeng.engine



/**
 * The status represents the current phase of the turn (e.g. who are we waiting on?). Each possible status is a
 * point in the game where a _player_ most make a decision or action.
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

    // TODO: do we want a 1-2s delay before revealing if a challenge is successful to add suspense? (I think yes)
    // This should probably be implemented on the client(?)

    /**
     * The current player is choosing which action to perform
     */
    SELECT_ACTION {
        inner class Event(val action: Action) : BaseEvent() {
            override fun update(state: State) {
                assert(state.status == Status.SELECT_ACTION)
                state.currentAction = action
                action.actor.coins -= action.cost
            }
        }

    },

    /**
     * Players are deciding whether to challenge the action
     */
    CHALLENGE,

    /**
     * the targeted player(s) are deciding whether to block the action
     * note: blocking comes after challenging, meaning you can challenge and then block an action, but you can't
     * block and then challenge (ie blocking implicitly accepts the legitimacy of the action)
     */
    BLOCK,

    /**
     * Players are deciding whether to challenge the block
     */
    CHALLENGE_BLOCK,

    /**
     * The affected player is deciding how to resolve the action (e.g. choosing a card to lose to assassin, etc.)
     * This step doesn't happen for actions that don't require player input to resolve
     */
    RESOLVE;


}
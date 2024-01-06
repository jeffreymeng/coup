package com.jeffkmeng.engine

import com.jeffkmeng.UIState


/**
 * Returns the element in the position after the given element in the list. If the given element is the last
 * element of the list, then the first element is returned.
 */
fun <T> List<T>.getAfter(el: T): T = this[(this.indexOf(el) + 1) % this.size]

/**
 * All the information for the current game state
 */
abstract class State {
    /**
     * The current players in the game.
     */
    abstract val players: List<Player>

    /**
     * The cards in the deck (i.e. all cards in the game not assigned to a player)
     */
    abstract val deck: List<Character>

    /**
     * The number of the current turn, starting from 1 and incrementing each time a turn has been completed (a turn
     * consists of a player attempting to perform an action, and either successfully performing the action or being
     * challenged/blocked).
     */
    abstract val turn: Int

    /**
     * The current player whose turn it is. This is not necessarily the player who is currently performing an action
     * (such as deciding to challenge, etc.).
     */
    abstract val currentTurnPlayer: Player

    /**
     * The set of players that we're waiting on.
     */
    abstract val waitingOn: Set<Player>

    /**
     * Process an event sent by a client at this state. All events must be handled, however if an event is illegal
     * given the current state, the function should throw IllegalEventException
     */
    abstract fun receiveMessage(message: Message): State

    /**
     * Returns the UI state for a given player.
     */
    fun getUIState(player: Player): UIState {
        return UIState(player.cards)
    }

    fun createNextTurnState() = SelectActionState.create(players, deck, turn + 1, players.getAfter(currentTurnPlayer))
}


/**
 * State where the current player is choosing which action to perform.
 */
data class SelectActionState private constructor(
    override val players: List<Player>,
    override val deck: List<Character>,
    override val turn: Int,
    override val currentTurnPlayer: Player
) : State() {
    override val waitingOn = setOf(currentTurnPlayer)

    companion object {
        fun create(players: List<Player>, deck: List<Character>, turn: Int, currentTurnPlayer: Player) =
            SelectActionState(players, deck, turn, currentTurnPlayer)
    }

    override fun receiveMessage(message: Message): State {
        if (message !is SelectActionMessage) {
            throw IllegalMessageException("Expected only a SelectActionEvent!")
        }
        message.action.actor.coins -= message.action.cost
        return ChallengeState.create(players, deck, turn, currentTurnPlayer, message.action)
    }
}

/**
 * State where a player must select a card to lose.
 */
data class SelectCardDeathState private constructor(
    override val players: List<Player>,
    override val deck: List<Character>,
    override val turn: Int,
    override val currentTurnPlayer: Player,
    val currentTurnAction: Action,
    /**
     * The player who must lose a card.
     */
    val player: Player,
    /**
     * A function that returns the state to continue to after this one (since this state can occur in between various
     * states). (this avoids updating the log until it's time to move on).
     */
    val nextStateFactory: () -> State
) : State() {
    override val waitingOn = setOf(currentTurnPlayer)

    companion object {
        fun create(
            players: List<Player>,
            deck: List<Character>,
            turn: Int,
            currentTurnPlayer: Player,
            currentTurnAction: Action,
            player: Player,
            nextStateFactory: () -> State
        ): State {
            return if (player.liveCards.size == 1) {
                // if the player has only one card, kill it automatically
                player.liveCards.first().isAlive = false
                nextStateFactory()
            } else {
                SelectCardDeathState(
                    players,
                    deck,
                    turn,
                    currentTurnPlayer,
                    currentTurnAction,
                    player,
                    nextStateFactory
                )
            }
        }

    }

    override fun receiveMessage(message: Message): State {
        if (message !is SelectCardDeathMessage || player != message.player) {
            throw IllegalMessageException("Expected only a SelectCardDeathEvent by $player!")
        }
        val card = message.player.cards.getOrNull(message.cardIndex)
            ?: throw InvalidMessageException("The selected card is not part of the player's list of characters")

        if (!card.isAlive) {
            throw InvalidMessageException("The selected card is not currently alive")
        }

        card.isAlive = false
        return nextStateFactory()
    }
}

/**
 * State where players are deciding whether to challenge the action.
 * @property mode - Whether the challenge is for an Action (e.g. challenging a duke's tax action) or for a Block (e.g.
 * challenging the block of stealing by an ambassador, etc.).
 */
data class ChallengeState private constructor(
    override val players: List<Player>,
    override val deck: List<Character>,
    override val turn: Int,
    override val currentTurnPlayer: Player,
    val currentTurnAction: Action,
    override val waitingOn: Set<Player> = players.toSet(),
) : State() {
    companion object {
        fun create(
            players: List<Player>,
            deck: List<Character>,
            turn: Int,
            currentTurnPlayer: Player,
            currentTurnAction: Action,
            waitingOn: Set<Player> = players.toSet()
        ): State {
            if (!currentTurnAction.canBeChallenged) {
                return BlockState.create(players, deck, turn, currentTurnPlayer, currentTurnAction)
            } else {
                return ChallengeState(players, deck, turn, currentTurnPlayer, currentTurnAction, waitingOn)
            }
        }
    }

    override fun receiveMessage(message: Message): State {
        if (message !is ChallengeDecisionMessage) {
            throw IllegalMessageException("Expected only a ChallengeDecisionEvent!")
        }
        // A player can still resubmit the message to challenge if they've previously decided not to challenge,
        // so there's no need to do any validation. (However, once the last player has declined a challenge, the
        // game will move on).
        if (message.isChallenging) {
            return if (currentTurnAction.isLegitimate()) {
                // the challenger has lost the challenge, so after this we can move on
                BlockState.create(players, deck, turn, currentTurnPlayer, currentTurnAction)
            } else {
                // the challenger has won the challenge, so after this we skip the rest of the action
                SelectCardDeathState.create(
                    players, deck, turn, currentTurnPlayer, currentTurnAction,
                    currentTurnPlayer, ::createNextTurnState
                )
            }
        } else {
            val newWaitingOn = waitingOn - message.player
            return if (newWaitingOn.isEmpty()) {
                BlockState.create(players, deck, turn, currentTurnPlayer, currentTurnAction)
            } else {
                this.copy(waitingOn = newWaitingOn)
            }
        }
    }

}


/**
 * State where the targeted player(s) are deciding whether to block the action
 * note: in this implementation (and the official rules), blocking comes after challenging, meaning you can challenge
 * and then block an action, but you can't block and then challenge (ie blocking implicitly accepts the
 * legitimacy of the action)
 */
data class BlockState private constructor(
    override val players: List<Player>,
    override val deck: List<Character>,
    override val turn: Int,
    override val currentTurnPlayer: Player,
    val currentTurnAction: Action,
    override val waitingOn: Set<Player> = players.toSet(),
) : State() {

    companion object {
        fun create(
            players: List<Player>,
            deck: List<Character>,
            turn: Int,
            currentTurnPlayer: Player,
            currentTurnAction: Action,
            waitingOn: Set<Player> = players.toSet()
        ): State {
            return if (currentTurnAction.canBeBlocked && waitingOn.isNotEmpty()) {
                BlockState(players, deck, turn, currentTurnPlayer, currentTurnAction, waitingOn)
            } else {
                ResolveState.create(players, deck, turn, currentTurnPlayer, currentTurnAction)
            }
        }
    }

    override fun receiveMessage(message: Message): State {
        if (message !is BlockDecisionMessage) {
            throw IllegalMessageException("Expected only a BlockDecisionMessage!")
        }

        if (message.isBlocking) {
            if (message.blockingCharacter == null) {
                throw IllegalMessageException("Expected a blockingCharacter to be defined when isBlocking is true")
            }
            return ChallengeBlockState.create(
                players,
                deck,
                turn,
                currentTurnPlayer,
                currentTurnAction,
                message.blockingCharacter
            )
        } else {
            if (message.blockingCharacter != null) {
                throw IllegalMessageException("Expected blockingCharacter to be null when isBlocking is false")
            }
            val newWaitingOn = waitingOn - message.player
            return if (newWaitingOn.isEmpty()) {
                ResolveState.create(players, deck, turn, currentTurnPlayer, currentTurnAction)
            } else {
                this.copy(waitingOn = newWaitingOn)
            }
        }
    }
}

/**
 * State where Players are deciding whether to challenge the block
 */
data class ChallengeBlockState private constructor(
    override val players: List<Player>,
    override val deck: List<Character>,
    override val turn: Int,
    override val currentTurnPlayer: Player,
    val currentTurnAction: Action,
    val blockingWith: Character,
    override val waitingOn: Set<Player> = players.toSet()
) : State() {
    companion object {
        fun create(
            players: List<Player>,
            deck: List<Character>,
            turn: Int,
            currentTurnPlayer: Player,
            currentTurnAction: Action,
            blockingWith: Character,
        ): ChallengeBlockState {
            assert(currentTurnAction.canBeBlocked) { "ChallengeBlock constructed on an action that can't be blocked." }
            return ChallengeBlockState(players, deck, turn, currentTurnPlayer, currentTurnAction, blockingWith)
        }

    }

    override fun receiveMessage(message: Message): State {
        if (message !is ChallengeDecisionMessage) { // the same message is used for BlockChallenge
            throw IllegalMessageException("Expected only a ChallengeDecisionMessage!")
        }
        if (message.isChallenging) {
            return if (currentTurnAction.canBeBlockedBy(currentTurnPlayer, blockingWith)) {
                // the challenger has lost the challenge -- the action is cancelled
                SelectCardDeathState.create(
                    players, deck, turn, currentTurnPlayer, currentTurnAction,
                    message.player, ::createNextTurnState
                )
            } else {
                // the blocker has lost the challenge -- others may still block
                // (if waitingOn is empty after this block, the game will immediately proceed)
                SelectCardDeathState.create(
                    players, deck, turn, currentTurnPlayer, currentTurnAction,
                    currentTurnPlayer
                ) {
                    BlockState.create(
                        players,
                        deck,
                        turn + 1,
                        players.getAfter(currentTurnPlayer),
                        currentTurnAction,
                        waitingOn = waitingOn - message.player
                    )
                }
            }
        } else {
            val newWaitingOn = waitingOn - message.player
            return if (newWaitingOn.isEmpty()) {
                BlockState.create(players, deck, turn, currentTurnPlayer, currentTurnAction)
            } else {
                this.copy(waitingOn = newWaitingOn)
            }
        }
    }

}
}

/**
 * State where the affected player is deciding how to resolve the action
 * (e.g. choosing a card to lose to assassin, etc.)
 */
data class ResolveState private constructor(
    override val players: List<Player>,
    override val deck: List<Character>,
    override val turn: Int,
    override val currentTurnPlayer: Player,
    val currentTurnAction: Action,
) : State() {
    // wait on all players, but expect clients to auto-move for the non-targeted players
    override val waitingOn: Set<Player> = setOf(currentTurnPlayer)

    companion object {
        fun create(
            players: List<Player>,
            deck: List<Character>,
            turn: Int,
            currentTurnPlayer: Player,
            currentTurnAction: Action
        ): State {
            if (currentTurnAction.getResolveWaitingOn().isEmpty()) {
                TODO() // perform the action

            } else {
                return ResolveState(players, deck, turn, currentTurnPlayer, currentTurnAction)
            }
        }
    }

    override fun receiveMessage(message: Message): State {
        TODO()
    }
}
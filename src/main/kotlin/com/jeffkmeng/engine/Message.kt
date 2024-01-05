package com.jeffkmeng.engine

/**
 * Base class for all messages. A `Message` is a piece of information sent by a client to the server, typically
 * a decision by the player to do something.
 *
 * @param player - The player sending the message.
 */
abstract class Message(val player: Player)

/**
 * @param action - The action that is being played
 */
class SelectActionMessage(val action: Action) : Message(action.actor)

/**
 * @param player - The player sending the event
 * @param cardIndex - The index of the card to kill within `player.cards`.
 */
class SelectCardDeathMessage(player: Player, val cardIndex: Int) : Message(player)

/**
 * @param player - The player sending the event
 * @param isChallenging - True iff the player has decided to challenge.
 */
class ChallengeDecisionMessage(player: Player, val isChallenging: Boolean) : Message(player)

/**
 * @param player - The player sending the event
 * @param isBlocking - True iff the player has decided to block.
 */
class BlockDecisionMessage(player: Player, val isBlocking: Boolean) : Message(player)

/**
 * @param player - The player sending the event
 * @param isBlocking - True iff the player has decided to challenge the block.
 */
class ChallengeBlockDecisionMessage(player: Player, val isChallenging: Boolean) : Message(player)

/**
 * Thrown when an event is received that cannot be performed on the current state by the sender of the event.
 */
class IllegalMessageException(message: String) : Exception(message)

/**
 * Thrown when an event is received that is in principle legal for the sender to perform, but contains invalid data.
 */
class InvalidMessageException(message: String) : Exception(message)
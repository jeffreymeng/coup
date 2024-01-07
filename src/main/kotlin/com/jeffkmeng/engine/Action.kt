package com.jeffkmeng.engine

import io.ktor.util.reflect.*
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor

/**
 * Ex. Tax, Income, Foreign Aid, Coup, etc
 */
abstract class Action(public val actor: Player) {
    abstract val id: String; // TODO: what is the id used for?
    abstract val canBeBlocked: Boolean // TODO: can we derive this from the set of actions?
    abstract val canBeChallenged: Boolean

    open val cost: Int = 0

    /**
     * Is the given player allowed to execute this action? ie. do they have enough money?
     */
    abstract fun isLegal(state: State): Boolean

    /**
     * If the given player executed the action and was challenged, do they lose a life?
     */
    fun isLegitimate(): Boolean =
        actor.liveCards.flatMap { it.actions }.any { this::class == it.reference }

    fun canBeBlockedBy(player: Player, card: Character): Boolean =
        this::class in card.blockedActions.map { it.reference } && card in player.liveCards

    /**
     * Performs the action and returns the next game state.
     * This function *may* mutate the state that's passed in.
     */
    abstract fun resolve(state: State): State

    /**
     * Returns the set of players who must respond for the action to resolve.
     * The default behavior is to return an empty set, which implies the action can be resolved immediately without any
     * player intervention.
     */
    open fun getResolveWaitingOn(): Set<Player> = emptySet()
}

interface TargetedAction {
    val target: Player
}


data class ActionManifest(
    val name: String,
    val description: String,
    val reference: KClass<out Action>
)

package com.jeffkmeng.engine

import io.ktor.util.reflect.*
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor

open class ActionPayload {

}

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

    /**
     * Performs the action, mutating state as necessary.
     */
    abstract fun resolve(state: State, payload: ActionPayload?)

    open fun getResolveWaitingOn() = setOf<Player>()
}

interface TargetedAction {
    val target: Player
}


data class ActionManifest(
    val name: String,
    val description: String,
    val reference: KClass<out Action>,
//    val isTargeted: Boolean = reference.isSubclassOf(TargetedAction::class) // TODO fix if broken
)

package com.jeffkmeng.engine

import io.ktor.util.reflect.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


/**
 * Ex. Tax, Income, Foreign Aid, Coup, etc
 */
abstract class Action(public val actor: Player) {
    abstract val id: String; // TODO: what is the id used for?

    /**
     * Is the given player allowed to execute this action? ie. do they have enough money?
     */
    abstract fun isLegal(state: State): Boolean

    /**
     * If the given player executed the action and was challenged, do they lose a life?
     */
    fun isLegitimate(state: State): Boolean =
        actor.liveCards.flatMap { it.actions }.any { this::class == it.reference }

    /**
     * Performs the action, mutating the state as necessary.
     */
    abstract fun resolve(state: State)
}

abstract class TargetedAction(actor: Player, val target: Player) : Action(actor)

data class ActionManifest(
    val name: String,
    val description: String,
    val reference: KClass<out Action>,
    val isTargeted: Boolean = reference.instanceOf(TargetedAction::class)
)

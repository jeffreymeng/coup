package com.jeffkmeng.basegame

import com.jeffkmeng.engine.*
import kotlin.reflect.KClass

class TaxAction(actor: Player) : Action(actor) {
    companion object {
        val MANIFEST = ActionManifest("Tax", "Collect 3 coins from the bank", TaxAction::class)
    }

    override val id = "tax"

    override fun isLegal(state: State) = true
    override fun resolve(state: State) {
        actor.coins += 3
    }
}

class DukeCharacter : Character() {
    override val actions: List<ActionManifest> = listOf(TaxAction.MANIFEST)
    override val blockedActions: List<Action> = listOf()
}

class AssassinateAction(actor: Player, target: Player) : TargetedAction(actor, target) {
    companion object {
        val MANIFEST = ActionManifest(
            "Assassinate",
            "For 3 coins, assassinate a player to cause them to lose an influence",
            AssassinateAction::class
        )
    }

    override val id = "assassinate"

    override fun isLegal(state: State) = actor.coins >= 3
    override fun resolve(state: State) {
        // TODO how do we implement this?
        // askTargetToSelectALifeToLose():Card.isAlive = false
    }
}

class AssassinCharacter : Character() {
    override val actions: List<ActionManifest> = listOf(AssassinateAction.MANIFEST)
    override val blockedActions: List<Action> = listOf()
}
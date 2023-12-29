package com.jeffkmeng.basegame

import com.jeffkmeng.engine.*

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
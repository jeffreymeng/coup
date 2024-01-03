package com.jeffkmeng.basegame

import com.jeffkmeng.engine.*

class IncomeAction(actor: Player) : Action(actor) {
    override val cost = 0
    override val canBeBlocked = false
    override val canBeChallenged = false

    companion object {
        val MANIFEST = ActionManifest("Income", "Collect 1 coin from the bank", TaxAction::class)
    }

    override val id = "income"

    override fun isLegal(state: State) = true
    override fun resolve(state: State, payload: ActionPayload?) {
        actor.coins += 1
    }
}
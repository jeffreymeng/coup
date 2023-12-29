package com.jeffkmeng.basegame

import com.jeffkmeng.engine.Action
import com.jeffkmeng.engine.ActionManifest
import com.jeffkmeng.engine.Player
import com.jeffkmeng.engine.State

class IncomeAction(actor: Player) : Action(actor) {
    companion object {
        val MANIFEST = ActionManifest("Income", "Collect 1 coin from the bank", TaxAction::class)
    }

    override val id = "income"

    override fun isLegal(state: State) = true
    override fun resolve(state: State) {
        actor.coins += 1
    }
}
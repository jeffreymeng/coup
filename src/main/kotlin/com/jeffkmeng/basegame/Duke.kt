package com.jeffkmeng.basegame

import com.jeffkmeng.engine.*

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
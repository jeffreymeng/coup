package com.jeffkmeng.basegame

import com.jeffkmeng.engine.*

class TaxAction(actor: Player) : Action(actor) {
    override val canBeBlocked = false
    override val canBeChallenged = true

    companion object {
        val MANIFEST = ActionManifest("Tax", "Collect 3 coins from the bank", TaxAction::class)
    }

    override val id = "tax"

    override fun isLegal(state: State) = true
    override fun resolve(state: State): State {
        actor.coins += 3
        return state.createNextTurnState()
    }

}

class DukeCharacter(id: Int) : Character(id) {
    override val actions: List<ActionManifest> = listOf(TaxAction.MANIFEST)
    override val blockedActions: Set<ActionManifest> = setOf()
}
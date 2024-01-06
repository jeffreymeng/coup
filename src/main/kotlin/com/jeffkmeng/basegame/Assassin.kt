package com.jeffkmeng.basegame

import com.jeffkmeng.engine.*

class AssassinatePayload(val card: Character) : ActionPayload() {

}

class AssassinateAction(actor: Player, override val target: Player) : Action(actor), TargetedAction {
    override val cost = 3
    override val canBeBlocked = true
    override val canBeChallenged = true

    companion object {
        val MANIFEST = ActionManifest(
            "Assassinate",
            "For 3 coins, assassinate a player to cause them to lose an influence",
            AssassinateAction::class
        )
    }

    override val id = "assassinate"

    override fun isLegal(state: State) = actor.coins >= 3

    override fun getResolveWaitingOn() = setOf(target)
    override fun resolve(state: State, payload: ActionPayload?) {
        if (payload !is AssassinatePayload) {
            throw Exception("Invalid payload")
        }
        val card =
            target.liveCards.find { it == payload.card } ?: throw Exception("Card to eliminate not found in player")
        card.isAlive = false
    }

}

class AssassinCharacter(id: Int) : Character(id) {
    override val actions: List<ActionManifest> = listOf(AssassinateAction.MANIFEST)
    override val blockedActions: Set<Action> = emptySet()
}
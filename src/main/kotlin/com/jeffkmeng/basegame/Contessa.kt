package com.jeffkmeng.basegame

import com.jeffkmeng.engine.*

class ContessaCharacter(id: Int) : Character(id) {
    override val actions: List<ActionManifest> = emptyList()
    override val blockedActions: Set<ActionManifest> = setOf(AssassinateAction.MANIFEST)
}
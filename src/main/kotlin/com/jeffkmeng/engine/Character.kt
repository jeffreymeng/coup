package com.jeffkmeng.engine;

/**
 * One character in Coup, like Captain or Duke
 */
abstract class Character(val id: Int) {

    /**
     * Is this character alive? This value is only valid
     * if this Character is part of a Player.
     * If this Character is part of GameEngine's "characters" property,
     * then the value of isAlive is garbage.
     */
    var isAlive = true

    /**
     * What actions can this character execute?
     * Ex. Duke can execute Tax
     */
    abstract val actions: List<ActionManifest>

    /**
     * What actions can this character block?
     * Ex. Duke can block Foreign Aid
     */
    abstract val blockedActions: Set<ActionManifest>

    override fun equals(other: Any?): Boolean {
        return other is Character && other.id == id
    }
}

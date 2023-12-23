/**
 * All the information for the current game state
 */
abstract class GameState {
    /**
     * Returns the UI state for a given player.
     */
    abstract fun getUIStateForPlayer(player: Player): UIState
}

/**
 * The actual game engine with all the logic. Every game will have one
 * instance of this in memory.
 *
 * Every game variant extends off a base GameEngine. The simplest variants
 * that have no logic changes can simply override GameVariant to change
 * characters / actions.
 */
abstract class GameEngine {
    /**
     * Variant information (ie. characters + actions) for this game
     */
    abstract val variant: GameVariant

    class GameEngine(val players: Array<Player>)

    // todo: things like executeAction(), etc
}

/**
 * Information for a player that the user provides to us when creating the game
 * Things like name, etc
 */
interface PlayerInformation {
    val name: String
}

/**
 * All the information necessary for one client to render the game
 * for one particular player. No sensitive information can be exposed here.
 */
interface UIState {
    // idk. also note that this interface will be duplicated here and again in TypeScript
    // and everything here needs to be serializable. Maybe protobuf?
}

/**
 * Information for a player for the actual game state.
 * Includes things like how much money they have, etc
 */
class Player {
    var money = 2
    class Player(val information: PlayerInformation, val characters: Array<Character>)
}

/**
 * Ex. Tax, Income, Foreign Aid, Coup, etc
 */
abstract class Action {
    abstract val id: String;

    /**
     * Is the given player allowed to execute this action? ie. do they have enough money?
     */
    abstract fun isLegal(gameState: GameState, player: Player): Boolean

    /**
     * If the given player executed the action and was challenged, do they lose a life?
     */
    abstract fun isLegitimate(gameState: GameState, player: Player): Boolean
}

/**
 * One character in Coup, like Captain or Duke
 */
abstract class Character {
    /**
     * What actions can this character execute?
     * Ex. Duke can execute Tax
     */
    abstract val actions: Array<Action>

    /**
     * What actions can this character block?
     * Ex. Duke can block Foreign Aid
     */
    abstract val blockedActions: Array<Action>
}

/**
 * Every variant implements this
 */
interface GameVariant {
    /**
     * Which characters are included in this game variant
     */
    val characters: Array<Character>

    /**
     * Any actions that aren't specific to a character (ex. Income, Coup)
     */
    val baseActions: Array<Action>
}
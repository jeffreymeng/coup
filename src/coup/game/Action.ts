import Player from "./Player";
import Game from "./Game";
import Role from "./Role";

export default abstract class Action {
    /**
     * The ID of the action. e.g. "tax"
     */
    public abstract id: string;

    /**
     * The name of the action, displayed in the UI. e.g. "Tax"
     */
    public abstract name: string;

    /**
     * The player performing the action.
     */
    public player: Player;

    /**
     * The target of the action, if one exists.
     */
    public target?: Player;

    /**
     * The game in which this action is to take place.
     */
    public game: Game;

    /**
     * The cost of the action, to be displayed in the UI.
     */
    public cost = 0;

    public constructor(game: Game, player: Player, target?: Player) {
        this.game = game;
        this.player = player;
        this.target = target;
    }

    /**
     * Perform the action. This happens after the payment, challenge, and block phases, so there is no need
     * to deduct the cost of the action from the initiating player.
     */
    public abstract resolve(): void;

    /**
     * Check if the action can be legally performed. An action is legal if it can be claimed by a player, even if
     * the player may be challenged. An action might be illegal if, for example, the player wouldn't have enough money
     * to perform the action.
     */
    public isLegal(): boolean {
        // if a player has 10 or more coins, the only legal action is coup
        // thus, for most actions (except for Coup and Assassinate), this is the only requirement for the move to be legal.
        return this.player.coins < 10 && this.player.coins > this.cost;
    }

    /**
     * Check if `player` is eligible to block this action (assuming they were to have the correct role). For example, only the
     * target of an assassination is eligible to claim contessa and attempt to block it, so canBeBlockedBy() should
     * return true for only the target of an assassination, and false for everybody else. However, nobody is eligible to
     * block the duke, so the function would return false for that action. Conversely, everybody other than the initiating
     * player is eligible to block foreign aid.
     *
     * @param player - the player doing the blocking
     */
    public canBeBlockedBy(blockingPlayer: Player): boolean {
        return false;
    }

    /**
     * Check if `role` blocks this action from occurring.
     * @param role - the role to check
     */
    public isBlockedBy(role: Role): boolean {
        return false;
    }
}

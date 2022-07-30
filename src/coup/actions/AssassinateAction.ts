import { Action, Game, Player, Role } from "../game";
import { Contessa } from "../roles";

export default class AssassinateAction extends Action {
    id = "assassinate";
    name = "Assassinate";
    cost = 3;

    public declare target: Player;

    /**
     * - Action is declared
     * - Action can be challenged
     * - Action is charged
     * - Action can be blocked
     * - Action is resolved
     */

    resolve() {
        this.target.removeInfluence();
    }

    canBeBlockedBy(blockingPlayer: Player) {
        return blockingPlayer.equals(this.target);
    }

    isBlockedBy(role: Role): boolean {
        return role instanceof Contessa;
    }
}

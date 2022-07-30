import { Action, Player, Role } from "../game";
import {Captain, Ambassador} from "../roles";

export default class StealAction extends Action {
    id = "steal";
    name = "Steal";

    public declare target: Player;

    /**
     * - Action is declared
     * - Action can be challenged
     * - Action is charged
     * - Action can be blocked
     * - Action is resolved
     */

    resolve() {
        this.target.removeCoins(2);
        this.player.addCoins(2);
    }

    canBeBlockedBy(blockingPlayer: Player) {
        return blockingPlayer.equals(this.target);
    }

    isBlockedBy(role: Role): boolean {
        return role instanceof Ambassador || role instanceof Captain;
    }
}

import { Action, Player, Role } from "../game";
import { Duke } from "../roles";

export default class ForeignAidAction extends Action {
    id = "exchange";
    name = "Exchange";

    resolve() {
        this.player.addCoins(2);
    }

    canBeBlockedBy(blockingPlayer: Player): boolean {
        return !blockingPlayer.equals(this.player);
    }

    isBlockedBy(role: Role): boolean {
        return role instanceof Duke;
    }
}

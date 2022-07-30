import { Action } from "../game";

export default class IncomeAction extends Action {
    id = "income";
    name = "Income";

    resolve() {
        this.player.addCoins(1);
    }
}

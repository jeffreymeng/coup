import { Action } from "../game";

export default class TaxAction extends Action {
    id = "tax";
    name = "Tax";

    resolve() {
        this.player.addCoins(3);
    }

}

import { Action } from "../game";

export default class ExchangeAction extends Action {
    id = "exchange";
    name = "Exchange";


    resolve() {
        this.player.swapInfluence();
    }


}
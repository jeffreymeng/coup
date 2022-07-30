import {Action, Role} from "../game";
import {ExchangeAction, TaxAction} from "../actions";

export default class Contessa extends Role {
    type = "contessa";
    name = "Contessa";

    public canTruthfullyClaim(action: Action): boolean {
        return false;
    }
}

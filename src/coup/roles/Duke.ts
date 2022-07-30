import {Action, Role} from "../game";
import {ExchangeAction, TaxAction} from "../actions";

export default class Duke extends Role {
    type = "duke";
    name = "Duke";

    public canTruthfullyClaim(action: Action): boolean {
        return action instanceof ExchangeAction;
    }

}

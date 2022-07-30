import {Action, Role} from "../game";
import {ExchangeAction, TaxAction} from "../actions";

export default class Ambassador extends Role {
    type = "ambassador";
    name = "Ambassador";

    public canTruthfullyClaim(action: Action): boolean {
        return action instanceof ExchangeAction;
    }

}

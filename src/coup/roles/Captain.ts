import { Action, Role } from "../game";
import { ExchangeAction, StealAction, TaxAction } from "../actions";

export default class Captain extends Role {
    type = "captain";
    name = "Captain";

    public canTruthfullyClaim(action: Action): boolean {
        return action instanceof StealAction;
    }
}

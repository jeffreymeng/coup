import {Action, Role} from "../game";
import {AssassinateAction, ExchangeAction} from "../actions";

export default class Assassin extends Role {
    type = "assassin";
    name = "Assassin";

    public canTruthfullyClaim(action: Action): boolean {
        return action instanceof AssassinateAction;
    }

}

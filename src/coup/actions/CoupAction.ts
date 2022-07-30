import { Action, Game, Player } from "../game";

export default class CoupAction extends Action {
    id = "coup";
    name = "Coup";
    cost = 10;

    declare public target: Player;


    resolve() {
        this.target.removeInfluence();
    }

    isLegal() {
        return this.player.coins >= 7;
    }
}

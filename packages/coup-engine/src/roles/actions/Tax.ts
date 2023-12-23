export default class TaxAction extends Action {
    title = "Tax";
    description = "Take 3 coins from the bank.";

    resolve(game: Game, player: Player) {
        // at this point, nobody has challenged.
        // game.getTarget()
        // getTarget returns if it hasn't been blocked?
    }
}

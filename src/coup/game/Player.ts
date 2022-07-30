import { Role, Game } from ".";

export default class Player {
    public readonly id: string;
    protected _coins = 2;
    protected _roles: Role[] = [];
    protected readonly game: Game;

    constructor(game: Game, id: string) {
        this.game = game;
        this.id = id;
    }

    /**
     * The number of coins the player currently has.
     */
    public get coins() {
        return this._coins;
    }

    public get roles() {
        return this._roles;
    }

    public addCoins(num: number) {
        this._coins += num;
    }

    public equals(other: Player): boolean {
        return other.id === this.id;
    }

    public removeCoins(num: number) {
        if (this._coins < num) {
            throw new Error(
                "This player does not have enough coins to remove."
            );
        }
        this._coins -= num;
    }

    /**
     * Removes an influence from the player. If the player has more influence than the amount they will lose,
     * they can choose which one to lose.
     *
     * @param num - the number of influence to lose.
     */
    public removeInfluence(num = 1) {

    }

    /**
     * Swaps influence with the deck. The player will draw `num` new influence, and then must choose which influence
     * to keep such that they return `num` influence back to the deck and maintain the same number of influence.
     * @param num
     */
    public swapInfluence(num = 2) {

    }
}

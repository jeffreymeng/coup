import Player from "./Player";

export default abstract class Game {
    public readonly players: Player[];

    /**
     * Whether the game has started yet. If this is false, then the game is in the 'lobby'. Otherwise, it is in progress.
     */
    public started = false;

    /**
     * The host of the game. The host has the ability to change settings, set players as 'away', kick players, and make
     * another player the host.
     * The host must be a player in the players array.
     */
    public host: Player;

    /**
     * Creates a new game.
     *
     * @param host - The host of the game.
     * @param otherPlayers - A list of players other than the host to add to the game.
     */
    constructor(host: Player, otherPlayers: Player[] = []) {
        this.host = host;
        this.players = [host, ...otherPlayers];
    }
}

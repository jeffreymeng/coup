abstract class Game {
    abstract readonly variantName: string;
    protected deck: Role[] = [];
    protected constructor() {
        this.initializeDeck();
    }

    /**
     * Initialize the deck by creating roles for the game.
     */
    abstract initializeDeck(): void;


}

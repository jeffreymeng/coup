import Action from "./Action";

export default abstract class Role {
    /**
     * The ID of the role. This string must be unique across all roles in this game, of the same and different types.
     */
    public readonly id: string;

    /**
     * A string used to identify the type of this role. e.g. "duke"
     */
    public abstract readonly type: string;

    /**
     * The name of the role, displayed in the ui. e.g. "Duke"
     */
    public abstract readonly name: string;

    protected _isAlive = true;

    /**
     * Whether this role is alive or dead. If the role is dead, then it must be assigned to a player and that player
     * must have lost this influence, meaning it can no longer be used to justify challenged actions. Otherwise, it
     */
    public get isAlive(): boolean {
        return this._isAlive;
    }

    constructor(id: string) {
        this.id = id;
    }

    /**
     * Mark this influence as dead.
     */
    public markAsDead() {
        this._isAlive = false;
    }

    /**
     * Whether this role can truthfully perform an action. i.e. If this role performs the given action and is challenged,
     * will it succeed?
     * @param action - The action to test
     */
    public abstract canTruthfullyClaim(action: Action): boolean;
}

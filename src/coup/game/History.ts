export type HistoryEntry = {
    type: "action";
};

export default class History {
    protected _entries: HistoryEntry[] = [];

    get entries() {
        return this._entries;
    }

    public getLatestEntry() {
        return this._entries[this._entries.length - 1];
    }

    public push(entry: HistoryEntry) {
        this._entries.push(entry);
    }
}

package com.jeffkmeng.history

import com.jeffkmeng.engine.Player

// TODO: 
class History : Iterable<HistoryEvent> {
    private val entries: MutableList<HistoryEvent> = mutableListOf()
    override fun iterator(): Iterator<HistoryEvent> = entries.iterator()

    fun log(event: HistoryEvent) = entries.add(event)
}


abstract class HistoryEvent {

    /**
     * A sample message for the event, derived from properties of the Event.
     */
    abstract fun message(): String
}

//@Serializable // TODO -- we don't really need serialization (since we don't need DEserialization which is the more
// complicated part), so we could also just export it as json somehow (e.g. export to map and then serialize that)
class TurnChangeEvent(val player: Player): HistoryEvent() {
    override fun message() = "It is now ${player.user.name}'s turn."
}

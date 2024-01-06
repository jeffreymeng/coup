package com.jeffkmeng.history

import com.jeffkmeng.engine.Player
import kotlinx.serialization.Serializable

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

//@Serializable // TODO -- we don't really need serialization, just to export it as json somehow (e.g. export to map
// then serialize the map
class TurnChangeEvent(val player: Player): HistoryEvent() {
    override fun message() = "It is now ${player.user.name}'s turn."
}

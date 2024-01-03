package com.jeffkmeng.engine

abstract class Event {
    abstract fun update(state: State)
}

class SelectActionEvent(val action: Action) : Event() {
    override fun update(state: State) {
        assert(state.status == Status.SELECT_ACTION)
        state.currentAction = action
        action.actor.coins -= action.cost
    }
}
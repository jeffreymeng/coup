package com.jeffkmeng

import com.jeffkmeng.basegame.DukeCharacter
import com.jeffkmeng.basegame.TaxAction
import com.jeffkmeng.engine.*
import kotlin.test.*


class EngineTest {
    @Test
    fun testBasic() {
        val alice = User("a", "Alice")
        val bob = User("b", "Bob")
        val game = Engine(
            listOf(alice, bob),
            listOf(DukeCharacter(0), DukeCharacter(1), DukeCharacter(2), DukeCharacter(3)),
            emptyList()
        )
        assertEquals(game.state.status, Status.SELECT_ACTION)
        assertEquals(game.getUIState(alice), UIState(listOf(DukeCharacter(0), DukeCharacter(1))))
        assertEquals(game.getUIState(bob), UIState(listOf(DukeCharacter(2), DukeCharacter(3))))

        game.receiveEvent(SelectActionEvent(TaxAction(game.getPlayer(alice))))
        assertEquals(game.state.status, Status.CHALLENGE)

        

        // game.onActionSelected()
    }
}

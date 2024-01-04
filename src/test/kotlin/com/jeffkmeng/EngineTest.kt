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
            List(4) { i -> DukeCharacter(i) },
            emptyList()
        )
        assertIs<SelectActionState>(game.state)
        assertEquals(game.state.deck, emptyList())
        assertEquals(game.getUIState(alice), UIState((0..1).map { DukeCharacter(it) }))
        assertEquals(game.getUIState(bob), UIState((2..3).map { DukeCharacter(it) }))

        game.receiveMessage(SelectActionMessage(TaxAction(game.getPlayer(alice))))
        assertIs<ChallengeState>(game.state)


        // game.onActionSelected()
    }
}

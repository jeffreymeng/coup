package com.jeffkmeng

import com.jeffkmeng.basegame.DukeCharacter
import com.jeffkmeng.basegame.TaxAction
import com.jeffkmeng.engine.*
import kotlin.test.*

class EngineTest {
    @Test
    fun testBasicFlow() {
        val uAlice = User("a", "Alice")
        val uBob = User("b", "Bob")
        val game = Engine(
            listOf(uAlice, uBob),
            List(4) { i -> DukeCharacter(i) },
            emptyList()
        )
        val alice = game.getPlayer(uAlice)
        val bob = game.getPlayer(uBob)

        assertIs<SelectActionState>(game.state)
        assertEquals(game.state.deck, emptyList())
        assertEquals(game.getUIState(uAlice), UIState((0..1).map { DukeCharacter(it) }))
        assertEquals(game.getUIState(uBob), UIState((2..3).map { DukeCharacter(it) }))

        game.receiveMessage(SelectActionMessage(TaxAction(alice)))
        assertIs<ChallengeState>(game.state)
        assertEquals(game.state.waitingOn, setOf(alice, bob))
        game.receiveMessage(ChallengeDecisionMessage(alice, false))
        game.receiveMessage(ChallengeDecisionMessage(bob, false))

        assertIs<BlockState>(game.state)

        // game.onActionSelected()
    }
}

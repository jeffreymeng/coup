package com.jeffkmeng

import com.jeffkmeng.basegame.DukeCharacter
import com.jeffkmeng.basegame.TaxAction
import com.jeffkmeng.engine.*
import kotlin.test.*


class EngineTest {
    fun hiddenCharacters(count: Int) = List(count) { UIHiddenCharacter(true) }
    fun createLiveDuke(id: Int) = UIVisibleCharacter.from(DukeCharacter(id))
    fun createDeadDuke(id: Int) = UIVisibleCharacter(false, DukeCharacter(id))
    fun dukesBetween(rangeStart: Int, rangeEnd: Int) = (rangeStart..rangeEnd).map(::createLiveDuke)

    @Test
    fun testBasicFlow() {
        val uAlice = User("alice", "Alice")
        val uBob = User("bob", "Bob")
        val game = Engine(
            listOf(uAlice, uBob),
            List(4) { i -> DukeCharacter(i) },
            emptyList()
        )
        val alice = game.getPlayer(uAlice)
        val bob = game.getPlayer(uBob)

        var aliceState = UIState(
            uAlice,
            listOf(
                UIPlayer(uAlice, dukesBetween(0, 1)),
                UIPlayer(uBob, hiddenCharacters(2))
            )
        )
        var bobState = UIState(
            uBob,
            listOf(
                UIPlayer(uAlice, hiddenCharacters(2)),
                UIPlayer(uBob, dukesBetween(2, 3))
            )
        )
        // initially, alice must select an action
        assertIs<SelectActionState>(game.state)
        assertEquals(emptyList(), game.state.deck)
        assertEquals(alice, game.state.currentTurnPlayer)
        assertEquals(setOf(alice), game.state.waitingOn)
        assertEquals(aliceState, game.getUIState(uAlice))
        assertEquals(bobState, game.getUIState(uBob))

        // Alice selects tax
        game.receiveMessage(SelectActionMessage(TaxAction(alice)))
        assertIs<ChallengeState>(game.state)
        assertEquals(setOf(bob), game.state.waitingOn)
        assertEquals(aliceState, game.getUIState(uAlice))
        assertEquals(bobState, game.getUIState(uBob))

        // Bob challenges, and loses, so must lose a card
        game.receiveMessage(ChallengeDecisionMessage(bob, true))
        assertIs<SelectCardDeathState>(game.state)
        assertEquals(setOf(bob), game.state.waitingOn)
        /* TODO -- make below tests work (if there are errors double check to make sure the tests are right first)

        // Bob chooses to lost his first duke
        game.receiveMessage(SelectCardDeathMessage(bob, 0))
        // we expect Bob's state to now be updated
        bobState = UIState(
            uBob,
            listOf(
                UIPlayer(uAlice, hiddenCharacters(2)),
                UIPlayer(uBob, listOf(createDeadDuke(2), createLiveDuke(3)))
            )
        )
        assertEquals(aliceState, game.getUIState(uAlice))
        assertEquals(bobState, game.getUIState(uBob))

        // Alice should have gotten the money from the Duke
        assertEquals(5, alice.coins)

        // Now it is bob's turn
        assertIs<SelectActionState>(game.state)
        assertEquals(bob, game.state.currentTurnPlayer)
        assertEquals(setOf(bob), game.state.waitingOn)

        // Bob selects tax
        game.receiveMessage(SelectActionMessage(TaxAction(alice)))
        assertIs<ChallengeState>(game.state)

        // Alice declines to challenge, so Bob's duke is automatically executed
        game.receiveMessage(ChallengeDecisionMessage(alice, false))
        assertIs<SelectActionState>(game.state)
        assertEquals(5, bob.coins)

         */

    }
}

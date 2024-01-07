package com.jeffkmeng

import com.jeffkmeng.basegame.*
import com.jeffkmeng.engine.*
import kotlin.test.*


class EngineTest {
    fun hiddenCharacters(vararg isAlive: Boolean) = isAlive.map { UIHiddenCharacter(it) }
    // todo: why does this need to accept an ID? also, maybe generalize
    // to accept an arbitrary type of character to create?
    fun createLiveDuke(id: Int) = UIVisibleCharacter(DukeCharacter(id))
    fun createDeadDuke(id: Int): UIVisibleCharacter {
        val duke = DukeCharacter(id)
        duke.isAlive = false
        return UIVisibleCharacter(duke)
    }
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
                UIPlayer(uAlice, dukesBetween(0, 1), 2),
                UIPlayer(uBob, hiddenCharacters(true, true), 2)
            )
        )
        var bobState = UIState(
            uBob,
            listOf(
                UIPlayer(uAlice, hiddenCharacters(true, true), 2),
                UIPlayer(uBob, dukesBetween(2, 3), 2)
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

        // Bob chooses to lost his first duke
        game.receiveMessage(SelectCardDeathMessage(bob, 0))
        // we expect Bob and Alice's state to now be updated
        aliceState = UIState(
            uAlice,
            listOf(
                UIPlayer(uAlice, listOf(createLiveDuke(0), createLiveDuke(1)), 5),
                UIPlayer(uBob, hiddenCharacters(false, true), 2)
            )
        )
        bobState = UIState(
            uBob,
            listOf(
                UIPlayer(uAlice, hiddenCharacters(true, true), 5),
                UIPlayer(uBob, listOf(createDeadDuke(2), createLiveDuke(3)), 2)
            )
        )
        assertEquals(aliceState, game.getUIState(uAlice))
        assertEquals(bobState, game.getUIState(uBob))

        // Alice should have gotten the money from the Duke
        assertEquals(5, alice.coins)

        // Now it is Bob's turn
        assertIs<SelectActionState>(game.state)
        assertEquals(bob, game.state.currentTurnPlayer)
        assertEquals(setOf(bob), game.state.waitingOn)

        // Bob selects tax
        game.receiveMessage(SelectActionMessage(TaxAction(bob)))
        assertIs<ChallengeState>(game.state)

        // Alice declines to challenge, so Bob's duke is automatically executed
        game.receiveMessage(ChallengeDecisionMessage(alice, false))
        assertIs<SelectActionState>(game.state)
        assertEquals(5, bob.coins)
    }

    @Test
    fun testAssassin() {
        val uAlice = User("alice", "Alice")
        val uBob = User("bob", "Bob")
        val game = Engine(
            listOf(uAlice, uBob),
            listOf(ContessaCharacter(0), AssassinCharacter(1), ContessaCharacter(2), AssassinCharacter(3)),
            emptyList()
        )
        val alice = game.getPlayer(uAlice)
        val bob = game.getPlayer(uBob)

        assertEquals(alice, game.state.currentTurnPlayer)

        // test that attempting to assassinate without enough money fails
        assertFailsWith(IllegalMessageException::class) {
            game.receiveMessage(SelectActionMessage(AssassinateAction(alice, bob)))
        }

        game.receiveMessage(SelectActionMessage(TaxAction(alice)))
        game.receiveMessage(ChallengeDecisionMessage(bob, false))

        game.receiveMessage(SelectActionMessage(TaxAction(bob)))
        game.receiveMessage(ChallengeDecisionMessage(alice, false))

        // test basic assassination: no challenges, no blocks
        game.receiveMessage(SelectActionMessage(AssassinateAction(alice, bob)))
        assertIs<ChallengeState>(game.state)
        game.receiveMessage(ChallengeDecisionMessage(bob, false))
        assertIs<BlockState>(game.state)
        game.receiveMessage(BlockDecisionMessage(bob, false, null))
        assertIs<SelectCardDeathState>(game.state)
        game.receiveMessage(SelectCardDeathMessage(bob, 0))
        assertIs<SelectActionState>(game.state)
        assertEquals(bob, game.state.currentTurnPlayer)

        // test double assassination: challenge fails, block succeeds
        game.receiveMessage(SelectActionMessage(AssassinateAction(bob, alice)))
        game.receiveMessage(ChallengeDecisionMessage(alice, true))
        // todo: bob needs to choose a new card
        assertIs<SelectCardDeathState>(game.state)
        assertEquals(mutableSetOf(alice), game.state.waitingOn)
        game.receiveMessage(SelectCardDeathMessage(alice, 1))
        assertIs<BlockState>(game.state)
        assertEquals(mutableSetOf(alice), game.state.waitingOn)
        // todo: why does contessaCharacter need an ID here?
        game.receiveMessage(BlockDecisionMessage(alice, true, ContessaCharacter(0)))
        assertIs<ChallengeBlockState>(game.state)
        assertEquals(mutableSetOf(bob), game.state.waitingOn)
        game.receiveMessage(ChallengeDecisionMessage(bob, true))

        // todo: game over, bob died

        var deadAssassin = AssassinCharacter(1)
        deadAssassin.isAlive = false
        val aliceState = UIState(
            uAlice,
            listOf(
                UIPlayer(uAlice, listOf(UIVisibleCharacter(ContessaCharacter(0)), UIVisibleCharacter(deadAssassin)), 2),
                UIPlayer(uBob, hiddenCharacters(false, false), 2)
            )
        )
        var deadContessa = ContessaCharacter(2)
        deadContessa.isAlive = false
        deadAssassin = AssassinCharacter(3)
        deadAssassin.isAlive = false
        val bobState = UIState(
            uBob,
            listOf(
                UIPlayer(uAlice, hiddenCharacters(true, false), 2),
                UIPlayer(uBob, listOf(UIVisibleCharacter(deadContessa), UIVisibleCharacter(deadAssassin)), 2)
            )
        )
        assertEquals(aliceState, game.getUIState(uAlice))
        assertEquals(bobState, game.getUIState(uBob))
    }
}

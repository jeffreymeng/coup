package com.jeffkmeng

import com.jeffkmeng.basegame.BaseGameEngine
import com.jeffkmeng.engine.Engine
import com.jeffkmeng.engine.State
import com.jeffkmeng.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class EngineTest {
    @Test
    fun testBasic() {
        val alice = User("a", "Alice")
        val bob = User("b", "Bob")
        val game = BaseGameEngine(
            listOf(alice, bob)
        )
        assertEquals(game.state.status, State.Status.SELECT_ACTION)

        game.receiveEvent()
//        assertEquals(game.getUIState(alice), UIState(......))

        // game.onActionSelected()
    }
}

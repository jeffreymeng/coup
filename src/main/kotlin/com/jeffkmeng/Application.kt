package com.jeffkmeng

import com.jeffkmeng.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    // embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
    //         .start(wait = true)

    val user1 = User(id = "123", name = "Nathan")
    val gameEngine = BaseGameEngine(listOf(user1))

    val initUIState = gameEngine.getUIStateForUser(user1)
    println(initUIState)
}

fun Application.module() {
    // configureSockets()
    // configureRouting()
}

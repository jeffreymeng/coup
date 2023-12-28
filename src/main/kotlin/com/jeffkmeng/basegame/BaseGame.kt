package com.jeffkmeng.basegame

import com.jeffkmeng.User
import com.jeffkmeng.engine.Action
import com.jeffkmeng.engine.Character
import com.jeffkmeng.engine.Engine


// todo: figure out how to prevent these variables from being exported? idk
val baseGameCharacters: List<Character> = listOf(
    DukeCharacter()
)

val baseGameActions: List<Action> = emptyList()

class BaseEngine(users: List<User>) : Engine(users, baseGameCharacters, baseGameActions) {
}

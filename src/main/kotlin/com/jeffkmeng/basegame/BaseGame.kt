package com.jeffkmeng.basegame

import com.jeffkmeng.User
import com.jeffkmeng.engine.ActionManifest
import com.jeffkmeng.engine.Character
import com.jeffkmeng.engine.Engine


// todo: figure out how to prevent these variables from being exported? idk
val baseGameCharacters: List<Character> = listOf(
    DukeCharacter(),
    AssassinCharacter()
)

val baseGameActions: List<ActionManifest> = emptyList()

class BaseGameEngine(users: List<User>) : Engine(users, baseGameCharacters, baseGameActions) {
}

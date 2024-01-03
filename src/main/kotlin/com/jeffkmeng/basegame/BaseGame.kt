package com.jeffkmeng.basegame

import com.jeffkmeng.User
import com.jeffkmeng.engine.Action
import com.jeffkmeng.engine.ActionManifest
import com.jeffkmeng.engine.Character
import com.jeffkmeng.engine.Engine


// todo: figure out how to prevent these variables from being exported? idk
val BGCharacters = listOf(
    ::DukeCharacter,
    ::AssassinCharacter
).flatMapIndexed { i, character -> MutableList(3) { j -> character.call(i * 3 + j) } }

val BGBaseActions = emptyList<ActionManifest>()

class BaseGameEngine(users: List<User>) : Engine(users, BGCharacters.shuffled(), BGBaseActions) {
}

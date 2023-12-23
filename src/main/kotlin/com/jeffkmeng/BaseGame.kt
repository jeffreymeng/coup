package com.jeffkmeng

class DukeCharacter : Character() {
    override val actions: List<Action> = emptyList()
    override val blockedActions: List<Action> = listOf()
}

// todo: figure out how to prevent these variables from being exported? idk
val baseGameCharacters: List<Character> = listOf(
    DukeCharacter()
)

val baseGameActions: List<Action> = emptyList()
class BaseGameEngine(users: List<User>) : GameEngine(users, baseGameCharacters, baseGameActions) {
}

package ru.thegamerdog.plufire.command

import ru.thegamerdog.plutail.command.CommandType

enum class XtCommandName(
    val key: String,
    val type: CommandType
) {
    JoinRoom("joinRoom", CommandType.JSON),
    SetNewPos("newPosition", CommandType.JSON),
    RoomObjectEvent("roomObjectEvent", CommandType.JSON);

    companion object {
        fun get(key: String, commandType: CommandType) = entries
            .singleOrNull { command -> command.key == key && command.type == commandType }
    }

    override fun toString(): String = "xt::$name"
}
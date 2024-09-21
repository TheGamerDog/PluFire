package ru.thegamerdog.plufire.command

import ru.thegamerdog.plutail.command.CommandType

enum class XtCommandName(
    val key: String,
    val type: CommandType
) {
    JOIN_ROOM("joinRoom", CommandType.JSON),
    SET_NEW_POS("newPosition", CommandType.JSON),
    ROOM_OBJECT_EVENT("roomObjectEvent", CommandType.JSON);

    companion object {
        fun get(key: String, commandType: CommandType) = entries
            .singleOrNull { command -> command.key == key && command.type == commandType }
    }

    override fun toString(): String = "xt::$name"
}
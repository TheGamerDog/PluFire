package ru.thegamerdog.plufire.listener

import nl.adaptivity.xmlutil.serialization.XML
import ru.thegamerdog.plutail.command.BodyXml
import ru.thegamerdog.plutail.command.DataObj
import ru.thegamerdog.plutail.command.MsgXml
import ru.thegamerdog.plutail.event.IEventConsumer
import ru.thegamerdog.plutail.event.room.JoinErrorEvent
import ru.thegamerdog.plutail.exception.room.FullRoomException
import ru.thegamerdog.plufire.PluFire
import ru.thegamerdog.plutail.util.DataObjTransform

class JoinErrorListener(private val plugin: PluFire) : IEventConsumer<JoinErrorEvent> {
    override val eventClass: Class<JoinErrorEvent> = JoinErrorEvent::class.java

    override fun onEvent(event: JoinErrorEvent) {
        if (event.user.zoneName != plugin.zoneName) return

        val vars = if (event.error is FullRoomException) {
            DataObjTransform.paramsToDataObj(
                mutableMapOf(
                    "_cmd" to "joinRoom",
                    "join" to "full"
                )
            )
        } else {
            DataObjTransform.paramsToDataObj(
                mutableMapOf(
                    "_cmd" to "joinRoom",
                    "join" to "error"
                )
            )
        }

        val res = MsgXml(
            t = "xt",
            body = BodyXml(
                action = "xtRes",
                r = -1,
                dataObj = XML.encodeToString(
                    DataObj(
                        `var` = vars
                    )
                )
            )
        )

        event.user.sendMessage(XML.encodeToString(res))
    }
}
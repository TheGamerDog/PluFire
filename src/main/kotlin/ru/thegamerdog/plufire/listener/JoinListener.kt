package ru.thegamerdog.plufire.listener

import nl.adaptivity.xmlutil.serialization.XML
import ru.thegamerdog.plutail.command.BodyXml
import ru.thegamerdog.plutail.command.DataObj
import ru.thegamerdog.plutail.command.MsgXml
import ru.thegamerdog.plutail.event.IEventConsumer
import ru.thegamerdog.plutail.event.room.PostJoinEvent
import ru.thegamerdog.plufire.PluFire
import ru.thegamerdog.plutail.util.DataObjTransform

class JoinListener(private val plugin: PluFire) : IEventConsumer<PostJoinEvent> {
    override val eventClass: Class<PostJoinEvent> = PostJoinEvent::class.java

    override fun onEvent(event: PostJoinEvent) {
        if (event.user.roomName == null || event.user.zoneName != plugin.zoneName) return

        val res = MsgXml(
            t = "xt",
            body = BodyXml(
                action = "xtRes",
                r = -1,
                dataObj = XML.encodeToString(
                    DataObj(
                        `var` = DataObjTransform.paramsToDataObj(
                            mutableMapOf(
                                "_cmd" to "joinRoom",
                                "join" to "ok"
                            )
                        )
                    )
                )
            )
        )

        event.user.sendMessage(XML.encodeToString(res))
    }
}
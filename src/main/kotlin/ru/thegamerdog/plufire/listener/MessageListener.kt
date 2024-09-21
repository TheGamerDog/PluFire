package ru.thegamerdog.plufire.listener

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import ru.thegamerdog.plutail.command.UserIdXml
import ru.thegamerdog.plutail.command.handler.TextXml
import ru.thegamerdog.plutail.event.IEventConsumer
import ru.thegamerdog.plutail.event.sys.PreMessageEvent
import ru.thegamerdog.plufire.PluFire

@Serializable
@XmlSerialName(value = "body")
data class TextBodyXml(
    val action: String,
    val r: Int,
    val txt: TextXml,
    val user: UserIdXml
)

@Serializable
@XmlSerialName(value = "msg")
data class TextMsgXml (
    val t: String,
    val body: TextBodyXml
)

class MessageListener(private val plugin: PluFire) : IEventConsumer<PreMessageEvent> {
    override val eventClass: Class<PreMessageEvent> = PreMessageEvent::class.java

    override fun onEvent(event: PreMessageEvent) {
        if (event.user.zoneName != plugin.zoneName) return

        if(event.text.startsWith("[TW|MSG][R]") && event.user.userModel!!.isMod) {
            broadcastRoom(event.user.userModel!!.id.value, event.user.roomName!!, event.text)
        } else if(event.text.startsWith("[TW|MSG][W]") && event.user.userModel!!.isMod) {
            broadcastZone(event.user.userModel!!.id.value, event.user.roomName!!, event.text)
        } else {
            broadcastMessage(event.user.userModel!!.id.value, event.user.roomName!!, event.text)
        }
    }

    private fun broadcastMessage(userId: Int, roomName: String, txt: String) {
        val zone = plugin.zoneManager.getZoneByName(plugin.zoneName)!!
        val room = zone.getRoom(roomName)
        val res = XML.encodeToString(TextMsgXml(
            t = "sys",
            body = TextBodyXml(
                action = "pubMsg",
                r = room!!.roomId,
                txt = TextXml(txt),
                user = UserIdXml(userId)
            )
        ))

        room.getAllUsers().forEach { it.sendMessage(res) }
    }

    private fun broadcastRoom(userId: Int, roomName: String, txt: String) {
        val zone = plugin.zoneManager.getZoneByName(plugin.zoneName)!!
        val room = zone.getRoom(roomName)
        val res = XML.encodeToString(TextMsgXml(
            t = "sys",
            body = TextBodyXml(
                action = "modMsg",
                r = room!!.roomId,
                txt = TextXml(txt),
                user = UserIdXml(userId)
            )
        ))

        room.getAllUsers().forEach { it.sendMessage(res) }
    }

    private fun broadcastZone(userId: Int, roomName: String, txt: String) {
        val zone = plugin.zoneManager.getZoneByName(plugin.zoneName)!!
        val room = zone.getRoom(roomName)
        val res = XML.encodeToString(TextMsgXml(
            t = "sys",
            body = TextBodyXml(
                action = "modMsg",
                r = room!!.roomId,
                txt = TextXml(txt),
                user = UserIdXml(userId)
            )
        ))

        zone.getAllUsers().forEach { it.sendMessage(res) }
    }
}
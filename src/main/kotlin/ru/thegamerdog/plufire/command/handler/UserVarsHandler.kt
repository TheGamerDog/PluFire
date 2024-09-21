package ru.thegamerdog.plufire.command.handler

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XML
import ru.thegamerdog.plutail.command.BodyXml
import ru.thegamerdog.plutail.command.DataObj
import ru.thegamerdog.plutail.command.MsgXml
import ru.thegamerdog.plufire.PluFire
import ru.thegamerdog.plufire.command.IXtCommandHandler
import ru.thegamerdog.plufire.command.XtCommandHandler
import ru.thegamerdog.plufire.command.XtCommandName
import ru.thegamerdog.plutail.user.IUser
import ru.thegamerdog.plutail.util.DataObjTransform

@Serializable
data class NewPositionJson(
    val posx: String,
    val posy: String,
    val animate: Boolean,
    val name: String,
    val updateSide: Boolean
)

class UserVarsHandler(plugin: PluFire) : IXtCommandHandler {
    private val zone = plugin.zoneManager.getZoneByName(plugin.zoneName)!!

    @XtCommandHandler(XtCommandName.SET_NEW_POS)
    fun setNewPos(user: IUser, roomId: Int, extensionName: String, data: NewPositionJson) {
        if (extensionName != "RoomExtension") return

        val res = XML.encodeToString(
            MsgXml(
                t = "xt",
                body = BodyXml(
                    action = "xtRes",
                    r = -1,
                    dataObj = XML.encodeToString(
                        DataObj(
                            `var` = DataObjTransform.paramsToDataObj(
                                mutableMapOf(
                                    "_cmd" to "newPosition",
                                    "posx" to data.posx,
                                    "posy" to data.posy,
                                    "animate" to data.animate,
                                    "user" to data.name,
                                    "updateSide" to data.updateSide
                                )
                            )
                        )
                    )
                )
            )
        )

        user.setUserVars(
            "posx" to data.posx.toFloat(),
            "posy" to data.posy.toFloat()
        )

        broadcastMessage(roomId, res)
    }

    private fun broadcastMessage(roomId: Int, res: String) {
        val room = zone.getRoom(roomId)

        room?.getAllUsers()?.forEach { it.sendMessage(res) }
    }
}
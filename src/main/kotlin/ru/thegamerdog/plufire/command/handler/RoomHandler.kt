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
data class JoinRoomJson(
    val fromRoom: String,
    val toRoom: String,
    val children: String? = null,
    val entrance: String? = null,
    val first: String? = null
)

@Serializable
data class RoomObjectJson(
    val name: String,
    val type: String,
    val userId: String? = null,

    val objectName: String? = null,
    val ext: String? = null,
    val eventId: Int? = null
)

class RoomHandler(private val plugin: PluFire) : IXtCommandHandler {
    @XtCommandHandler(XtCommandName.JOIN_ROOM)
    fun joinRoom(user: IUser, roomId: Int, extensionName: String, data: JoinRoomJson) {
        if (extensionName != "WorldsExtension") return

        user.setUserVars(
            "fromRoom" to data.fromRoom
        )
        user.updateUserVars()

        // TODO: Add XML msg, send actual time, set sky by time, kick from game if time > 9 PM (set in config)
        user.sendMessage("<msg t='xt'><body action='xtRes' r='-1'><![CDATA[<dataObj><var n='sky' t='n'>1</var><var n='_cmd' t='s'>sky</var><var n='time' t='s'>23:01</var></dataObj>]]></body></msg>")

        plugin.zoneManager.getZoneByName(plugin.zoneName)!!.joinToRoom(user, data.toRoom)
    }

    // TODO: Add behavior for all objects
    @XtCommandHandler(XtCommandName.ROOM_OBJECT_EVENT)
    fun roomObjEvent(user: IUser, roomId: Int, extensionName: String, data: RoomObjectJson) {
        if (extensionName != "RoomExtension") return // TODO: Add extension handler (?)
        if (data.name == "eventPlayer") return getResultForEventPlayer(user, data)

        val obj = when (data.name) {
            "secretDoor" -> {
                if (data.type == "showCode") {
                    DataObjTransform.objToDataObj(
                        "data",
                        mutableMapOf(
                            "name" to "secretDoor",
                            "type" to "showCode"
                        )
                    )
                } else {
                    DataObjTransform.objToDataObj(
                        "data",
                        mutableMapOf(
                            "name" to "secretDoor",
                            "type" to "showCode"
                        )
                    )
                }
            }

            else -> {
                DataObjTransform.objToDataObj(
                    "data",
                    mutableMapOf(
                        "name" to "secretDoor",
                        "type" to "showCode"
                    )
                )
            }
        }

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
                                    "_cmd" to "roomObjectEvent"
                                )
                            ),
                            obj = arrayListOf(obj)
                        )
                    )
                )
            )
        )

        user.sendMessage(res)
    }

    private fun getResultForEventPlayer(user: IUser, data: RoomObjectJson) {

    }
}
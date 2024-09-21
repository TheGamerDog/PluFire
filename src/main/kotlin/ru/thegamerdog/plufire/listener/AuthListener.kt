package ru.thegamerdog.plufire.listener


import nl.adaptivity.xmlutil.serialization.XML
import org.jetbrains.exposed.sql.transactions.transaction
import ru.thegamerdog.plutail.command.BodyXml
import ru.thegamerdog.plutail.command.DataObj
import ru.thegamerdog.plutail.command.MsgXml
import ru.thegamerdog.plutail.event.IEventConsumer
import ru.thegamerdog.plutail.event.sys.PostAuthEvent
import ru.thegamerdog.plufire.PluFire
import ru.thegamerdog.plufire.db.UserModel
import ru.thegamerdog.plutail.user.IUser
import ru.thegamerdog.plutail.util.DataObjTransform

class AuthListener(private val plugin: PluFire) : IEventConsumer<PostAuthEvent> {
    override val eventClass: Class<PostAuthEvent> = PostAuthEvent::class.java

    override fun onEvent(event: PostAuthEvent) {
        if (event.user.zoneName != plugin.zoneName) return

        if (event.user.userModel == null) {
            val vars = DataObjTransform.paramsToDataObj(
                mutableMapOf(
                    "_cmd" to "varifyUserDetails",
                    "status" to "expired"
                )
            )

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

            return event.user.sendMessage(XML.encodeToString(res))
        }

        val userModel = getUserModel(event.user)
        event.user.xtUserModel = userModel

        val vars = if (userModel.banned) {
            DataObjTransform.paramsToDataObj(
                mutableMapOf(
                    "_cmd" to "varifyUserDetails",
                    "status" to "banned"
                )
            )
        } else if (event.user.userModel!!.isMod) {
            // Currently it is only used to load the moderator panel
            // TODO: Add additional password for mod account
            //  Add privilege flags by bitwise manipulation

            DataObjTransform.paramsToDataObj(
                mutableMapOf(
                    "_cmd" to "varifyUserDetails",
                    "status" to "passTry"
                )
            )
        } else if (event.user.isSpec) {
            DataObjTransform.paramsToDataObj(
                mutableMapOf(
                    "_cmd" to "varifyUserDetails",
                    "status" to "expired"
                )
            )
        } else {
            DataObjTransform.paramsToDataObj(
                mutableMapOf(
                    "_cmd" to "varifyUserDetails",

                    "pName" to event.user.userModel!!.username,
                    "pId" to event.user.userModel!!.id.value,
                    "status" to "success"
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
        if (userModel.banned) event.user.destroy()
    }

    private fun getUserModel(user: IUser): UserModel {
        val db = plugin.zoneManager.getZoneByName(plugin.zoneName)!!.db

        return transaction(db) {
            return@transaction UserModel.findById(user.userModel!!.id)!!
        }
    }
}
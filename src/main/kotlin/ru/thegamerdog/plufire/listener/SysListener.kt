package ru.thegamerdog.plufire.listener

import ru.thegamerdog.plutail.command.SysCommandName
import ru.thegamerdog.plutail.event.IEventConsumer
import ru.thegamerdog.plutail.event.packet.PostPacketEvent
import ru.thegamerdog.plufire.PluFire
import ru.thegamerdog.plufire.db.UserModel

class SysListener(private val plugin: PluFire) : IEventConsumer<PostPacketEvent> {
    override val eventClass: Class<PostPacketEvent> = PostPacketEvent::class.java

    override fun onEvent(event: PostPacketEvent) {
        if (event.commandName != SysCommandName.GetRoomList || event.user.zoneName != plugin.zoneName) return

        val userModel = event.user.xtUserModel as UserModel

        event.user.setUserVars(
            "petColor" to userModel.petColor,
            "petUserID" to userModel.id.value,
            "petId" to userModel.petId,
            "gender" to "human", // The client does not use this value, lol
            "petAge" to 666, // That too
            "houseType" to "foru", // TODO: Create houses
            "houseEnv" to 1,
            "houseColor" to "0xFFFFFF",
            "houseUrl" to "https://blablabla.com",
            "gees" to userModel.gees,
            "petToken" to userModel.petToken
        )

        plugin.zoneManager.getZoneByName(plugin.zoneName)!!.joinToRoom(event.user, "Lobby")
        event.user.updateUserVars()
        //event.user.sendMessage("<msg t='sys'><body action='joinOK' r='1'><pid id='${event.user.userModel!!.id.value}'/><vars /><uLs r='1'><u i='${event.user.userModel!!.id.value}' m='1'><n><![CDATA[${event.user.userModel!!.username}]]></n><vars></vars></u></uLs></body></msg>")
        //event.user.sendMessage("<msg t='xt'><body action='xtRes' r='-1'><![CDATA[<dataObj><var n='_cmd' t='s'>joinRoom</var><var n='join' t='s'>dLink</var></dataObj>]]></body></msg>")
        //event.user.sendMessage("<msg t='sys'><body action='uVarsUpdate' r='-1'><vars><var n='player_info_6' t='s'><![CDATA[{\"sectionId\":6,\"itemId\":0,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":30,\"dy\":-15,\"dr\":0,\"ds\":1.2,\"use_itemIdOverride\":false}]]></var><var n='chosenHouse' t='s'><![CDATA[378]]></var><var n='player_info_5' t='s'><![CDATA[{\"sectionId\":5,\"itemId\":194,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":42,\"dr\":0,\"ds\":1.3,\"use_itemIdOverride\":false}]]></var><var n='ace' t='n'><![CDATA[2]]></var><var n='player_info_4' t='s'><![CDATA[{\"sectionId\":4,\"itemId\":188,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":18,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}]]></var><var n='player_info_3' t='s'><![CDATA[{\"sectionId\":3,\"itemId\":184,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":35,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}]]></var><var n='petId' t='n'><![CDATA[9]]></var><var n='petToken' t='s'><![CDATA[NaN]]></var><var n='color' t='s'><![CDATA[0xFF0000]]></var><var n='gender' t='s'><![CDATA[geonadad]]></var><var n='player_info_9' t='s'><![CDATA[{\"sectionId\":9,\"itemId\":0,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}]]></var><var n='player_info_8' t='s'><![CDATA[{\"sectionId\":8,\"itemId\":0,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}]]></var><var n='player_info_7' t='s'><![CDATA[{\"sectionId\":7,\"itemId\":196,\"itemIdOverride\":-1,\"colorIndex\":100,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}]]></var><var n='postAge' t='n'><![CDATA[666]]></var><var n='player_info_18' t='s'><![CDATA[{\"sectionId\":18,\"itemId\":0,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}]]></var><var n='player_info_19' t='s'><![CDATA[{\"sectionId\":19,\"itemId\":0,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}]]></var><var n='houseEnv' t='s'><![CDATA[1]]></var><var n='player_info_16' t='s'><![CDATA[{\"sectionId\":16,\"itemId\":0,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}]]></var><var n='houseUrl' t='s'><![CDATA[https://www.cocolani.net/ch/worlds/play/index.php?house=378]]></var><var n='player_info_17' t='s'><![CDATA[{\"sectionId\":17,\"itemId\":0,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}]]></var><var n='player_info_14' t='s'><![CDATA[{\"sectionId\":14,\"itemId\":0,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}]]></var><var n='player_info_15' t='s'><![CDATA[{\"sectionId\":15,\"itemId\":207,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}]]></var><var n='lolat' t='n'><![CDATA[200]]></var><var n='player_info_12' t='s'><![CDATA[{\"sectionId\":12,\"itemId\":0,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}]]></var><var n='player_info_13' t='s'><![CDATA[{\"sectionId\":13,\"itemId\":0,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}]]></var><var n='player_info_10' t='s'><![CDATA[{\"sectionId\":10,\"itemId\":182,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}]]></var><var n='player_info_11' t='s'><![CDATA[{\"sectionId\":11,\"itemId\":202,\"itemIdOverride\":-1,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}]]></var><var n='add' t='s'><![CDATA[null]]></var><var n='houseType' t='s'><![CDATA[foru]]></var><var n='petUserId' t='s'><![CDATA[378]]></var><var n='houseColor' t='s'><![CDATA[0xFFFFFF]]></var><var n='guid' t='s'><![CDATA[thegamerdog]]></var><var n='anim' t='s'><![CDATA[none|none|none]]></var><var n='player_info_2' t='s'><![CDATA[{\"sectionId\":2,\"itemId\":225,\"itemIdOverride\":-1,\"colorIndex\":1,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}]]></var><var n='player_info_1' t='s'><![CDATA[{\"sectionId\":1,\"itemId\":0,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}]]></var></vars><user id='1' /></body></msg>")
    }
}
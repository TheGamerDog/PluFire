package ru.thegamerdog.plufire.listener

import ru.thegamerdog.plutail.event.IEventConsumer
import ru.thegamerdog.plutail.event.sys.SetUVarsEvent
import ru.thegamerdog.plufire.PluFire

class SetUVarsListener(private val plugin: PluFire) : IEventConsumer<SetUVarsEvent> {
    override val eventClass: Class<SetUVarsEvent> = SetUVarsEvent::class.java

    override fun onEvent(event: SetUVarsEvent) {
        if (event.user.zoneName != plugin.zoneName || event.user.userVars["gameMode"] != null) return

        // TODO: Add clothes, avatar editor
        val list: ArrayList<Pair<String, Any?>> = arrayListOf(
            "currentAvatar" to 2,
            "avatar_data_1" to "{\"sectionId\":1,\"itemId\":0,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}",
            "avatar_data_2" to "{\"sectionId\":2,\"itemId\":225,\"itemIdOverride\":-1,\"colorIndex\":1,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}",
            "avatar_data_3" to "{\"sectionId\":3,\"itemId\":184,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":35,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}",
            "avatar_data_4" to "{\"sectionId\":4,\"itemId\":188,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":18,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}",
            "avatar_data_5" to "{\"sectionId\":5,\"itemId\":194,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":42,\"dr\":0,\"ds\":1.3,\"use_itemIdOverride\":false}",
            "avatar_data_6" to "{\"sectionId\":6,\"itemId\":0,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":30,\"dy\":-15,\"dr\":0,\"ds\":1.2,\"use_itemIdOverride\":false}",
            "avatar_data_7" to "{\"sectionId\":7,\"itemId\":196,\"itemIdOverride\":-1,\"colorIndex\":100,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}",
            "avatar_data_8" to "\"sectionId\":8,\"itemId\":0,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}",
            "avatar_data_9" to "{\"sectionId\":9,\"itemId\":0,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}",
            "avatar_data_10" to "{\"sectionId\":10,\"itemId\":182,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}",
            "avatar_data_11" to "{\"sectionId\":11,\"itemId\":202,\"itemIdOverride\":-1,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}",
            "avatar_data_12" to "{\"sectionId\":12,\"itemId\":0,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}",
            "avatar_data_13" to "{\"sectionId\":13,\"itemId\":0,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}",
            "avatar_data_14" to "{\"sectionId\":14,\"itemId\":0,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}",
            "avatar_data_15" to "{\"sectionId\":15,\"itemId\":207,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}",
            "avatar_data_16" to "{\"sectionId\":16,\"itemId\":0,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}",
            "avatar_data_17" to "{\"sectionId\":17,\"itemId\":0,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}",
            "avatar_data_18" to "{\"sectionId\":18,\"itemId\":0,\"itemIdOverride\":-1,\"colorIndex\":0,\"dx\":0,\"dy\":0,\"dr\":0,\"ds\":1,\"use_itemIdOverride\":false}",
        )

        event.vars.`var`!!.forEach {
            var value = when(it.t) {
                "s" -> it.value
                "n" -> it.value.toInt()
                "b" -> it.value == "1" || it.value == "true"
                else -> null
            }

            if (it.n == "anim" && it.value == "none") {
                value = "none|none|none"
            }

            list.add(it.n to value)
        }

        event.user.setUserVars(*list.toTypedArray())
        event.isCanceled = true
    }
}
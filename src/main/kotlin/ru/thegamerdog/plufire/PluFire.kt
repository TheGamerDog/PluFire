package ru.thegamerdog.plufire

import org.reflections.Reflections
import org.reflections.scanners.Scanners
import ru.thegamerdog.plufire.command.IXtCommandHandler
import ru.thegamerdog.plufire.command.XtCommandRegistry
import ru.thegamerdog.plufire.listener.*
import ru.thegamerdog.plutail.plugin.Plugin
import ru.thegamerdog.plutail.plugin.PluginConfig
import java.net.URLClassLoader
import kotlin.reflect.KClass

class PluFire(
    private val data: PluginConfig,
    classLoader: URLClassLoader?
) : Plugin(data, classLoader) {
    val zoneName = "TweegeeWorldsRU" // TODO: Check by config
    val commandRegistry = XtCommandRegistry(this)

    override fun onEnable() {
        logger.info("${data.name} plugin started")

        val reflections = Reflections("ru.thegamerdog.plufire.command.handler")

        reflections.get(Scanners.SubTypes.of(IXtCommandHandler::class.java).asClass<IXtCommandHandler>())
            .forEach { type ->
                val handlerType = type.kotlin as KClass<IXtCommandHandler>

                commandRegistry.registerHandlers(handlerType)
                logger.debug("Registered command handler: ${handlerType.simpleName}")
            }

        registerEvents(
            SysListener(this),
            AuthListener(this),
            XtListener(this),
            JoinListener(this),
            JoinErrorListener(this),
            SetUVarsListener(this),
            MessageListener(this)
        )
    }
}
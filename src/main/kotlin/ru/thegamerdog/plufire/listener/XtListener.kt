package ru.thegamerdog.plufire.listener

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import nl.adaptivity.xmlutil.serialization.XML
import ru.thegamerdog.plutail.command.CommandType
import ru.thegamerdog.plutail.command.MsgJson
import ru.thegamerdog.plutail.command.MsgXml
import ru.thegamerdog.plutail.event.IEventConsumer
import ru.thegamerdog.plutail.event.xt.XtMessageEvent
import ru.thegamerdog.plufire.PluFire
import ru.thegamerdog.plufire.command.XtCommandName
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.functions
import kotlin.reflect.full.primaryConstructor

class XtListener(private val plugin: PluFire) : IEventConsumer<XtMessageEvent> {
    override val eventClass: Class<XtMessageEvent> = XtMessageEvent::class.java

    override fun onEvent(event: XtMessageEvent) {
        if (event.zoneName != plugin.zoneName) return

        plugin.logger.trace("Received xt raw message: ${event.message}")

        when (val commandType = CommandType.get(event.message[0])) {
            CommandType.XML -> {
                // Bypass for passing a string for sys and subsequent processing using special types
                val xmlArr = event.message.split("<")
                val outMessage = "<" + xmlArr[1] + "<" + xmlArr[2] + "</body></msg>"
                val inMessage = event.message
                    .replace("<" + xmlArr[1] + "<" + xmlArr[2], "")
                    .replace("</body></msg>", "")
                    .replace(Regex("^<!\\[CDATA\\["), "")
                    .replace(Regex("]]>$"), "")

                val xmlMessage = XML.decodeFromString(MsgXml.serializer(), outMessage)
                val commandName = XtCommandName.get(xmlMessage.body.action, commandType) ?: return

                val handler = plugin.commandRegistry.getHandler(commandName) ?: return
                val args = mutableMapOf<KParameter, Any?>()

                val instance = handler.type.primaryConstructor!!.call(plugin)
                args += mapOf(
                    Pair(
                        handler.function.parameters.single { parameter -> parameter.kind == KParameter.Kind.INSTANCE },
                        instance
                    ),
                    Pair(
                        handler.function.parameters.filter { parameter -> parameter.kind == KParameter.Kind.VALUE }[0],
                        event.user
                    ),
                    Pair(
                        handler.function.parameters.filter { parameter -> parameter.kind == KParameter.Kind.VALUE }[1],
                        xmlMessage.body.r
                    )
                )

                if (handler.args.isNotEmpty()) {
                    val clazz = handler.args[0].type.classifier as KClass<*>
                    val staticObj = clazz.companionObject!!
                    val serializer = staticObj.functions.find { it.name == "serializer" }!!
                        .call(staticObj.objectInstance) as DeserializationStrategy<*>

                    args[handler.function.parameters.filter { parameter -> parameter.kind == KParameter.Kind.VALUE }[2]] =
                        XML.decodeFromString(serializer, inMessage)
                }

                try {
                    handler.function.callBy(args)
                } catch (exception: Throwable) {
                    val targetException = if (exception is InvocationTargetException) exception.cause else exception
                    plugin.logger.error("Failed to call $commandName handler:\n$targetException")
                }
            }

            CommandType.STR -> {
                val commandArgs = event.message.split("%").drop(2).toMutableList()
                val commandName = XtCommandName.get(commandArgs.removeAt(1), commandType) ?: return

                val handler = plugin.commandRegistry.getHandler(commandName) ?: return
                val args = mutableMapOf<KParameter, Any?>()

                val instance = handler.type.primaryConstructor!!.call(plugin)
                args += mapOf(
                    Pair(
                        handler.function.parameters.single { parameter -> parameter.kind == KParameter.Kind.INSTANCE },
                        instance
                    ),
                    Pair(
                        handler.function.parameters.filter { parameter -> parameter.kind == KParameter.Kind.VALUE }[0],
                        event.user
                    ),
                    Pair(
                        handler.function.parameters.filter { parameter -> parameter.kind == KParameter.Kind.VALUE }[1],
                        commandArgs[1]
                    )
                )

                commandArgs.forEachIndexed { index, value ->
                    args[handler.function.parameters.filter { parameter -> parameter.kind == KParameter.Kind.VALUE }[index + 2]] =
                        value
                }

                try {
                    handler.function.callBy(args)
                } catch (exception: Throwable) {
                    val targetException = if (exception is InvocationTargetException) exception.cause else exception
                    plugin.logger.error("Failed to call $commandName handler:\n$targetException")
                }
            }

            CommandType.JSON -> {
                val jsonMessage = Json.decodeFromString<MsgJson>(event.message)
                val commandName = XtCommandName.get(jsonMessage.b.c, commandType) ?: return

                val handler = plugin.commandRegistry.getHandler(commandName) ?: return
                val args = mutableMapOf<KParameter, Any?>()

                val instance = handler.type.primaryConstructor!!.call(plugin)
                args += mapOf(
                    Pair(
                        handler.function.parameters.single { parameter -> parameter.kind == KParameter.Kind.INSTANCE },
                        instance
                    ),
                    Pair(
                        handler.function.parameters.filter { parameter -> parameter.kind == KParameter.Kind.VALUE }[0],
                        event.user
                    ),
                    Pair(
                        handler.function.parameters.filter { parameter -> parameter.kind == KParameter.Kind.VALUE }[1],
                        jsonMessage.b.r
                    )
                )

                if (handler.args.size == 2) {
                    val clazz = handler.args[1].type.classifier as KClass<*>
                    val staticObj = clazz.companionObject!!
                    val serializer = staticObj.functions.find { it.name == "serializer" }!!
                        .call(staticObj.objectInstance) as DeserializationStrategy<*>

                    args[handler.function.parameters.filter { parameter -> parameter.kind == KParameter.Kind.VALUE }[2]] =
                        jsonMessage.b.x
                    args[handler.function.parameters.filter { parameter -> parameter.kind == KParameter.Kind.VALUE }[3]] =
                        Json.decodeFromJsonElement(serializer, jsonMessage.b.p)
                }

                try {
                    handler.function.callBy(args)
                } catch (exception: Throwable) {
                    val targetException = if (exception is InvocationTargetException) exception.cause else exception
                    plugin.logger.error("Failed to call $commandName handler:\n$targetException")
                }
            }

            else -> plugin.logger.error("Invalid command type")
        }
    }
}
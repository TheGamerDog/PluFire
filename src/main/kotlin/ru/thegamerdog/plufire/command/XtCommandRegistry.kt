package ru.thegamerdog.plufire.command

import ru.thegamerdog.plufire.PluFire
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation

interface IXtCommandRegistry {
    fun <T : IXtCommandHandler> registerHandlers(type: KClass<T>)
    fun getHandler(name: XtCommandName): XtCommandHandlerInfo?
}

class XtCommandRegistry(private val plugin: PluFire) : IXtCommandRegistry {
    private val commands: MutableList<XtCommandHandlerInfo> = mutableListOf()

    override fun getHandler(name: XtCommandName): XtCommandHandlerInfo? {
        return commands.singleOrNull { command -> command.name == name }
    }

    override fun <T : IXtCommandHandler> registerHandlers(type: KClass<T>) {
        type.declaredMemberFunctions.forEach { function ->
            val commandHandler = function.findAnnotation<XtCommandHandler>() ?: return@forEach
            val args = function.parameters
                .filter { parameter -> parameter.kind == KParameter.Kind.VALUE }
                .drop(2)

            val description = XtCommandHandlerInfo(type, function, commandHandler.name, args)
            commands.add(description)

            plugin.logger.debug(
                "Discovered command handler: {} -> {}::{}",
                commandHandler.name,
                type.qualifiedName,
                function.name
            )
        }
    }
}
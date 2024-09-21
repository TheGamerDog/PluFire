package ru.thegamerdog.plufire.command

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

data class XtCommandHandlerInfo(
    val type: KClass<out IXtCommandHandler>,
    val function: KFunction<*>,
    val name: XtCommandName,
    val args: List<KParameter>
)
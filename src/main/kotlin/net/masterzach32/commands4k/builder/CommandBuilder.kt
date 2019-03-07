package net.masterzach32.commands4k.builder

import discord4j.core.`object`.util.Permission
import net.masterzach32.commands4k.*

fun createCommand(name: String, builderFunc: CommandBuilder.() -> Unit): Command {
    return CommandBuilder(name).apply(builderFunc).build()
}

class CommandBuilder(private val name: String): Builder<Command> {

    var aliases = emptyList<String>()
    var hidden = false
    var args = emptyList<ArgumentInfo<Any>>()
    var scope = Command.Scope.ALL
    var botPerm = BotPermissions.NORMAL
    var discordPerms = emptyList<Permission>()
    var toggleTypingStatus = false
    var exec: CommandInvokeHandler = DEFAULT_EXEC
    lateinit var description: String
    var details: String? = null
    var processArgs: Boolean = true

    fun args(argsBuilder: ArgsBuilder.() -> Unit) {
        args = ArgsBuilder().apply(argsBuilder).build()
    }

    /*fun helpText(docsBuilder: DocsBuilder.() -> Unit) {
        cmdDocs = DocsBuilder().apply(docsBuilder).build()
    }*/

    fun onEvent(eventBehavior: EventBuilder.() -> Unit) {
        exec = EventBuilder().apply(eventBehavior)/*.also { listeners.addAll(it.listeners) }*/.build()
    }

    override fun build(): Command {
        if (!processArgs && args.size > 1)
            throw IllegalStateException("Command $name can only have 1 arg if processArgs is false.")
        return Command(name, aliases, description, args, details, hidden, processArgs, scope, botPerm, discordPerms, exec)
    }
}
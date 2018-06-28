package net.masterzach32.commands4k.builder

import net.masterzach32.commands4k.Command
import net.masterzach32.commands4k.CommandDocs
import net.masterzach32.commands4k.Permission
import sx.blah.discord.api.events.Event
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.obj.Permissions

fun createCommand(name: String, builderFunc: CommandBuilder.() -> Unit): Command {
    return CommandBuilder(name).apply(builderFunc).build()
}

class CommandBuilder(private val name: String): Builder<Command> {

    var aliases = emptyList<String>()
    var hidden = false
    var scope = Command.Scope.ALL
    var botPerm = Permission.NORMAL
    var discordPerms = emptyList<Permissions>()
    var toggleTypingStatus = false
    private var cmdDocs = CommandDocs()
    private var exec = DEFAULT_BEHAVIOR
    private val listeners = mutableListOf<IListener<Event>>()

    fun helpText(docsBuilder: DocsBuilder.() -> Unit) {
        cmdDocs = DocsBuilder().apply(docsBuilder).build()
    }

    fun onEvent(eventBehavior: EventBuilder.() -> Unit) {
        exec = EventBuilder().apply(eventBehavior).also { listeners.addAll(it.listeners) }.build()
    }

    override fun build(): Command {
        return Command(name, aliases, hidden, scope, botPerm, discordPerms, cmdDocs, exec, listeners)
    }
}
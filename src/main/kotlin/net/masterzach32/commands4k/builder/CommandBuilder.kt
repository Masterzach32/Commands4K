package net.masterzach32.commands4k.builder

import net.masterzach32.commands4k.Command
import net.masterzach32.commands4k.CommandDocs
import net.masterzach32.commands4k.Permission
import sx.blah.discord.api.events.Event
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.obj.Permissions

fun createCommand(name: String, builderFunc: CommandBuilder.() -> Unit): Command {
    val builder = CommandBuilder(name)

    builder.builderFunc()

    return builder.build()
}

class CommandBuilder(private val name: String): Builder<Command> {

    var aliases = emptyList<String>()
    var hidden = false
    var scope = Command.Scope.ALL
    var botPerm = Permission.NORMAL
    var discordPerms = emptyList<Permissions>()
    var toggleTypingStatus = false
    private var cmdDocs = CommandDocs()
    private var exec = DEFAULT_EXEC
    private val listeners = mutableListOf<IListener<Event>>()

    fun helpText(helpBuilder: HelpBuilder.() -> Unit) {
        val builder = HelpBuilder()

        builder.helpBuilder()

        cmdDocs = builder.build()
    }

    fun onEvent(eventWrapperFunc: EventBuilder.() -> Unit) {
        val builder = EventBuilder()

        builder.eventWrapperFunc()
        listeners.addAll(builder.listeners)

        exec = builder.build()
    }

    override fun build(): Command {
        return Command(name, aliases, hidden, scope, botPerm, discordPerms, cmdDocs, exec, listeners)
    }
}
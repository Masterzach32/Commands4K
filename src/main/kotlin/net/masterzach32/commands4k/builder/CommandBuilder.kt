package net.masterzach32.commands4k.builder

import net.masterzach32.commands4k.AdvancedMessageBuilder
import net.masterzach32.commands4k.Command
import net.masterzach32.commands4k.CommandDocs
import net.masterzach32.commands4k.Permission
import sx.blah.discord.handle.obj.Permissions

fun createCommand(name: String, builderFunc: CommandBuilder.() -> Unit): Command {
    val builder = CommandBuilder(name)

    builderFunc.invoke(builder)

    return builder.build()
}

class CommandBuilder(private val name: String): Builder<Command> {

    private var aliases = emptyList<String>()
    private var hidden = false
    private var scope = Command.Scope.ALL
    private var botPerm = Permission.NORMAL
    private var discordPerms = emptyList<Permissions>()
    private var toggleTypingStatus = false
    private var cmdDocs = CommandDocs()
    private var exec = DEFAULT_EXEC

    fun aliases(vararg aliasArray: String) {
        aliases = aliasArray.toList()
    }

    fun hidden(bool: () -> Boolean) {
        hidden = bool.invoke()
    }

    fun botPerm(perm: () -> Permission) {
        botPerm = perm.invoke()
    }

    fun discordPerm(vararg permsArray: Permissions) {
        discordPerms = permsArray.toList()
    }

    fun scope(scope: () -> Command.Scope) {
        this.scope = scope.invoke()
    }

    fun helpText(helpBuilder: HelpBuilder.() -> Unit) {
        val builder = HelpBuilder()

        helpBuilder.invoke(builder)

        cmdDocs = builder.cmdHelp
    }

    fun onEvent(eventWrapperFunc: EventBuilder.() -> Unit) {
        val builder = EventBuilder()

        builder.eventWrapperFunc()

        exec = builder.build()
    }

    override fun build(): Command {
        return Command(name, aliases, hidden, scope, botPerm, discordPerms, cmdDocs, exec)
    }
}
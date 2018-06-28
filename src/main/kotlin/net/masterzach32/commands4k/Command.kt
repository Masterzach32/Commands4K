package net.masterzach32.commands4k

import net.masterzach32.commands4k.builder.DEFAULT_BEHAVIOR
import net.masterzach32.commands4k.builder.EventHandler
import net.masterzach32.commands4k.builder.EventWrapper
import sx.blah.discord.api.events.Event
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.Permissions

open class Command (
        val name: String,
        vararg aliases: String,
        val hidden: Boolean = false,
        val scope: Scope = Scope.ALL,
        val botPerm: Permission = Permission.NORMAL,
        val discordPerms: List<Permissions> = listOf(),
        val help: CommandDocs = CommandDocs(),
        val listeners: List<IListener<Event>> = listOf()
) {

    @Deprecated("Use CommandBuilder's onEvent function to specify command scope.")
    enum class Scope {
        ALL, GUILD, PRIVATE
    }

    val aliases = mutableListOf<String>()

    internal var defaultExec = DEFAULT_BEHAVIOR

    init {
        if(aliases.isEmpty())
            throw IllegalArgumentException("$name must have at least one alias!")
        this.aliases.addAll(aliases)
    }

    constructor(
            name: String,
            aliases: List<String>,
            hidden: Boolean = false,
            scope: Scope = Scope.ALL,
            botPerm: Permission = Permission.NORMAL,
            discordPerms: List<Permissions> = listOf(),
            docs: CommandDocs,
            exec: EventHandler,
            listeners: List<IListener<Event>>
    ) : this(
            name,
            *aliases.toTypedArray(),
            hidden = hidden,
            scope = scope,
            botPerm = botPerm,
            discordPerms = discordPerms,
            help = docs,
            listeners = listeners
    ) {
        defaultExec = exec
    }

    open fun execute(cmdUsed: String, args: Array<String>, event: MessageReceivedEvent,
                         builder: AdvancedMessageBuilder): AdvancedMessageBuilder? {
        val wrapper = EventWrapper(cmdUsed, args, event, builder)
        return defaultExec.invoke(wrapper)
    }

    fun matchesAlias(cmd: String): Boolean {
        return aliases.any { it == cmd.toLowerCase() }
    }

    override fun toString(): String {
        return "$name - $aliases\nMinimum Permission: $botPerm"
    }
}
package net.masterzach32.commands4k

import net.masterzach32.commands4k.builder.DEFAULT_EXEC
import net.masterzach32.commands4k.builder.EventHandler
import net.masterzach32.commands4k.builder.EventWrapper
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.Permissions

open class Command(val name: String, vararg aliases: String, val hidden: Boolean = false,
                       val scope: Scope = Scope.ALL, val botPerm: Permission = Permission.NORMAL,
                       val discordPerms: List<Permissions> = listOf()) {

    constructor(name: String, aliases: List<String>, hidden: Boolean = false,
                scope: Scope = Scope.ALL, botPerm: Permission = Permission.NORMAL,
                discordPerms: List<Permissions> = listOf(), docs: CommandDocs,
                exec: EventHandler) : this(name, *aliases.toTypedArray(), hidden = hidden,
            scope = scope, botPerm = botPerm, discordPerms = discordPerms) {
        help = docs
        defaultExec = exec
    }

    enum class Scope {
        ALL, GUILD, PRIVATE
    }

    val aliases = mutableListOf<String>()
    var help: CommandDocs

    internal var defaultExec = DEFAULT_EXEC

    init {
        if(aliases.isEmpty())
            throw IllegalArgumentException("$name must have at least one alias!")
        aliases.forEach { this.aliases.add(it) }

        help = CommandDocs()
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
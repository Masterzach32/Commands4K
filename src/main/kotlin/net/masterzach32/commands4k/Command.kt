package net.masterzach32.commands4k

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.Permissions

abstract class Command(val name: String, vararg aliases: String, val hidden: Boolean = false,
                       val scope: Scope = Scope.ALL, val botPerm: Permission = Permission.NORMAL,
                       val discordPerms: List<Permissions> = listOf()) {

    enum class Scope {
        ALL, GUILD, PRIVATE
    }

    val aliases = mutableListOf<String>()
    val help = CommandHelp()

    init {
        if(aliases.isEmpty())
            throw IllegalArgumentException("$name must have at least one alias!")
        aliases.forEach { this.aliases.add(it) }
    }

    abstract fun execute(cmdUsed: String, args: Array<String>, event: MessageReceivedEvent,
                         builder: AdvancedMessageBuilder): AdvancedMessageBuilder?

    fun matchesAlias(cmd: String): Boolean {
        return aliases.any { it == cmd.toLowerCase() }
    }

    override fun toString(): String {
        return "$name - $aliases\nMinimum Permission: $botPerm"
    }
}
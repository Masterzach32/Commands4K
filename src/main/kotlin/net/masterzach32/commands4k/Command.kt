package net.masterzach32.commands4k

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.Permissions
import sx.blah.discord.util.MessageBuilder
import java.util.*

abstract class Command(val name: String, vararg aliases: String, val hidden: Boolean = false,
                       val scope: Scope = Scope.ALL, val botPerm: Permission = Permission.NORMAL,
                       val discordPerms: List<Permissions> = listOf()) {

    enum class Scope {
        ALL, GUILD, PRIVATE
    }

    val aliases: MutableList<String> = ArrayList()

    init {
        if(aliases.isEmpty())
            throw IllegalArgumentException("$name must have at least one alias!")
        aliases.forEach { this.aliases.add(it) }
    }

    abstract fun execute(cmdUsed: String, args: Array<String>, event: MessageReceivedEvent,
                         builder: AdvancedMessageBuilder): AdvancedMessageBuilder?

    abstract fun getCommandHelp(usage: MutableMap<String, String>)

    fun matchesAlias(cmd: String): Boolean {
        aliases.forEach {
            if(cmd.toLowerCase() == it)
                return true
        }
        return false
    }

    override fun toString(): String {
        return "$name - ${Arrays.toString(aliases.toTypedArray())}\nMinimum Permission: $botPerm"
    }
}
package net.masterzach32.commands4k

import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import java.util.*

class CommandManager : IListener<MessageReceivedEvent> {

    private val commandList: MutableList<Command> = ArrayList()
    private val quickLookup: MutableMap<String, Command> = HashMap()

    @Synchronized
    fun getCommandList(): List<Command> = commandList

    @Synchronized
    fun getCommand(cmd: String): Command? = quickLookup[cmd]

    @Synchronized
    fun sortCommands(): CommandManager {
        commandList.sortWith(Comparator { one, two ->
            if(one.permission.ordinal != two.permission.ordinal) {
                if(one.permission.ordinal > two.permission.ordinal)
                    return@Comparator 1
                else
                    return@Comparator -1
            } else
                return@Comparator one.aliases[0].compareTo(two.aliases[0])
        })
        return this
    }

    @Synchronized
    fun add(cmd: Command): CommandManager {
        commandList.forEach { other ->
            cmd.aliases
                    .filter { other.matchesAlias(it) }
                    .forEach { throw IllegalArgumentException("Duplicate aliases: {${other.name}: $it} {${cmd.name}: $it}") }
        }
        commandList.add(cmd)

        for (alias in cmd.aliases)
            quickLookup.put(alias, cmd)

        return sortCommands()
    }

    @Synchronized
    fun remove(cmd: Command): CommandManager {
        commandList.remove(cmd)
        cmd.aliases.forEach { quickLookup.remove(it) }
        return sortCommands()
    }

    override fun handle(event: MessageReceivedEvent) {

    }
}
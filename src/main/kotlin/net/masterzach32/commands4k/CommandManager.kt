package net.masterzach32.commands4k

import java.util.*

open class CommandManager {

    private val commandList: MutableList<Command> = ArrayList()
    private val quickLookup: MutableMap<String, Command> = HashMap()

    @Synchronized
    fun getCommandList(): List<Command> = commandList

    @Synchronized
    fun getCommand(cmd: String): Command? = quickLookup[cmd]

    @Synchronized
    fun sortCommands(): CommandManager {
        commandList.sortWith(Comparator { one, two ->
            if(one.botPerm.ordinal != two.botPerm.ordinal) {
                if(one.botPerm.ordinal > two.botPerm.ordinal)
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
}
package net.masterzach32.commands4k

import sx.blah.discord.api.events.EventDispatcher

open class CommandManager(val dispatcher: EventDispatcher) {

    private val commandList = mutableListOf<Command>()
    private val quickLookup = mutableMapOf<String, Command>()

    @Synchronized
    fun getCommandList(): List<Command> = commandList

    @Synchronized
    fun getCommand(cmd: String): Command? = quickLookup[cmd]

    @Synchronized
    fun sortCommands() {
        commandList.sortWith(Comparator { one, two ->
            if(one.botPerm != two.botPerm) {
                if(one.botPerm > two.botPerm)
                    return@Comparator 1
                else
                    return@Comparator -1
            } else
                return@Comparator one.aliases.first().compareTo(two.aliases.first())
        })
    }

    @Synchronized
    fun add(vararg cmds: Command) {
        val toAdd = cmds.toList()
        toAdd.forEach {cmd ->
            commandList.forEach { other ->
                cmd.aliases
                        .filter { other.matchesAlias(it) }
                        .forEach { throw IllegalArgumentException("Duplicate aliases: {${other.name}: $it} {${cmd.name}: $it}") }
            }
            commandList.add(cmd)

            cmd.aliases.forEach { quickLookup[it] = cmd }
            cmd.listeners.forEach { dispatcher.registerListener(it) }
        }
    }

    @Synchronized
    fun remove(cmd: Command) {
        commandList.remove(cmd)
        cmd.aliases.forEach { quickLookup.remove(it) }
        cmd.listeners.forEach { dispatcher.unregisterListener(it) }
    }
}
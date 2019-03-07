package net.masterzach32.commands4k

open class CommandManager {

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
            // verify argument order is valid
            var hasInfinite = false
            var hasOptional = false
            cmd.args.forEach {
                if (hasInfinite)
                    throw IllegalArgumentException("No other arguments may come after an infinite argument.")
                if (!it.required)
                    hasOptional = true
                else if (hasOptional)
                    throw IllegalArgumentException("Required arguments may not come after optional arguments.")
                if (it.infinite)
                    hasInfinite = true
            }
            commandList.add(cmd)

            cmd.aliases.forEach { quickLookup[it] = cmd }
        }
    }

    @Synchronized
    fun remove(cmd: Command) {
        commandList.remove(cmd)
        cmd.aliases.forEach { quickLookup.remove(it) }
    }
}
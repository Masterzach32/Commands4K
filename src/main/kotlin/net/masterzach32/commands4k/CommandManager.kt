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
            if(one.botPerm.ordinal != two.botPerm.ordinal) {
                if(one.botPerm.ordinal > two.botPerm.ordinal)
                    return@Comparator 1
                else
                    return@Comparator -1
            } else
                return@Comparator one.aliases[0].compareTo(two.aliases[0])
        })
    }

    @Synchronized
    fun add(cmd: Command) {
        commandList.forEach { other ->
            cmd.aliases
                    .filter { other.matchesAlias(it) }
                    .forEach { throw IllegalArgumentException("Duplicate aliases: {${other.name}: $it} {${cmd.name}: $it}") }
        }
        commandList.add(cmd)

        cmd.aliases.forEach { quickLookup[it] = cmd }
    }

    @Synchronized
    fun remove(cmd: Command) {
        commandList.remove(cmd)
        cmd.aliases.forEach { quickLookup.remove(it) }
    }
}
package net.masterzach32.commands4k.builder

import net.masterzach32.commands4k.CommandDocs

class DocsBuilder : Builder<CommandDocs> {

    var description: String? = null

    val usage = mutableMapOf<String, String>()

    override fun build(): CommandDocs {
        val cmdHelp = CommandDocs()
        cmdHelp.desc = description
        cmdHelp.usage.putAll(usage)
        return cmdHelp
    }
}
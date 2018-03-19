package net.masterzach32.commands4k.builder

import net.masterzach32.commands4k.CommandDocs

class HelpBuilder : Builder<CommandDocs> {

    val cmdHelp = CommandDocs()

    fun description(desc: () -> String) {
        cmdHelp.desc = desc.invoke()
    }

    fun usage(paramFormat: String, desc: () -> String) {
        cmdHelp.usage[paramFormat] = desc.invoke()
    }

    override fun build(): CommandDocs {
        return cmdHelp
    }
}
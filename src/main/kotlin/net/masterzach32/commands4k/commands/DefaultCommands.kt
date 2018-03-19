package net.masterzach32.commands4k.commands

import net.masterzach32.commands4k.Command
import net.masterzach32.commands4k.GREY
import net.masterzach32.commands4k.Permission
import net.masterzach32.commands4k.builder.createCommand
import sx.blah.discord.util.EmbedBuilder

val HelpCommand2: Command = createCommand("Help") {

    aliases("help", "h")

    botPerm { Permission.NONE }

    helpText {
        description { "Display all available commands, or get more info on a specific command." }
        usage("") { "Display all available commands." }
        usage("<command>") { "Display detailed information about a specific command." }
    }

    onEvent {
        val embed = EmbedBuilder().withColor(GREY)
    }
}
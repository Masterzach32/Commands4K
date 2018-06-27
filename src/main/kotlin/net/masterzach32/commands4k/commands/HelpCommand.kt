package net.masterzach32.commands4k.commands

import net.masterzach32.commands4k.*
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IUser
import sx.blah.discord.util.EmbedBuilder

/*
 * Commands4K - Created on 11/14/2017
 * Author: Zach Kozar
 * 
 * This code is licensed under the GNU GPL v3
 * You can find more info in the LICENSE file at the project root.
 */

/**
 * @author Zach Kozar
 * @version 11/14/2017
 */
internal class HelpCommand(
        private val cmds: CommandManager,
        private val commandPrefix: (IGuild?) -> String,
        private val botPermission: IUser.(IGuild?) -> Permission
) : Command("Help", "help", "h", botPerm = Permission.NONE) {

    init {
        help.desc = "Display all available commands, or get more info on a specific command."
        help.usage[""] = "Display a list of commands."
        help.usage["<command>"] = "Display detailed information about that command."
    }

    override fun execute(cmdUsed: String, args: Array<String>, event: MessageReceivedEvent,
                         builder: AdvancedMessageBuilder): AdvancedMessageBuilder {
        val embed = EmbedBuilder().withColor(GREY)
        val cmdPrefix = commandPrefix(event.guild)
        val botPermission = event.author.botPermission(event.guild)

        if (args.isEmpty()) {
            if (!event.channel.isPrivate) {
                embed.withDesc("${event.author.mention()} A list of commands has been sent to you privately.")
                AdvancedMessageBuilder(event.channel).withEmbed(embed).build()
            }
            builder.withChannel(event.client.getOrCreatePMChannel(event.author))
            embed.withTitle("Help and Info:")
            embed.withDesc("")
            var i = 0
            while (i <= botPermission.ordinal) {
                var str = ""
                cmds.getCommandList()
                        .filter { it.botPerm == Permission.values()[i] }
                        .forEach { str += cmdPrefix + it.aliases.first() + "\n" }
                if (str.isEmpty())
                    str = "There are no commands for this permission level."
                embed.appendField(Permission.values()[i].name, str, true)
                i++
            }
            embed.withDesc(
                    "**Note**: Command prefixes may be different per guild!\n" +
                            "**Permissions**: ${Permission.values().toList()}\n" +
                            "To view more information for a command, use `${cmdPrefix}help <command>`\n\n" +
                            "Note you can only see the commands available to you with your permission " +
                            "**$botPermission** in **${event.guild?.name}**")
            builder.withEmbed(embed)
        } else {
            val cmd = cmds.getCommandList().firstOrNull { it.aliases.contains(args[0]) }
            if (cmd == null)
                builder.withEmbed(embed.withColor(RED).withDesc("No command found with alias `${args[0]}`").build())
            else {
                embed.withColor(GREY)
                embed.withTitle("Command: **${cmd.name}**")

                if (cmd.help.hasDesc())
                    embed.appendField("Description:", "${cmd.help.desc}", false)

                if (cmd.help.hasUsage()) {
                    var str = ""
                    cmd.help.usage.forEach {
                        str += "\n`$cmdPrefix${args[0]} ${it.key}` ${it.value}"
                    }
                    embed.appendField("Usage:", str, false)
                }

                if (!cmd.help.hasHelpText())
                    embed.withDesc("No help text.")

                embed.appendField("Aliases:", "${cmd.aliases}", true)
                embed.appendField("Scope:", "${cmd.scope}", true)
                embed.appendField("Permission Required:", "${cmd.botPerm}", true)

                builder.withEmbed(embed)
            }

        }
        return builder
    }
}

package net.masterzach32.commands4k

import net.masterzach32.commands4k.commands.HelpCommand
import net.masterzach32.commands4k.events.CommandExecutedEvent
import org.slf4j.LoggerFactory
import sx.blah.discord.api.events.EventDispatcher
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IUser
import sx.blah.discord.handle.obj.Permissions
import sx.blah.discord.util.EmbedBuilder
import sx.blah.discord.util.MissingPermissionsException
import sx.blah.discord.util.RequestBuffer

/*
 * Commands4K - Created on 11/10/2017
 * Author: Zach Kozar
 * 
 * This code is licensed under the GNU GPL v3
 * You can find more info in the LICENSE file at the project root.
 */

/**
 * @author Zach Kozar
 * @version 11/10/2017
 */
class CommandListener(
        dispatcher: EventDispatcher,
        private val commandPrefix: (IGuild?) -> String,
        private val botPermission: IUser.(IGuild?) -> Permission
) : CommandManager(dispatcher), IListener<MessageReceivedEvent> {

    private val logger = LoggerFactory.getLogger("Commands4K")

    init {
        add(HelpCommand(this, commandPrefix, botPermission))

        dispatcher.registerListener(this)
    }

    override fun handle(event: MessageReceivedEvent) {
        if (event.author.isBot)
            return
        val commandPrefix = commandPrefix(event.guild)
        if (!event.message.content.startsWith(commandPrefix))
            return

        val identifier: String
        val params: Array<String>

        val tmp = event.message.content
                .substring(commandPrefix.length)
                .split(" ").toTypedArray()
        identifier = tmp[0]
        params = tmp.copyOfRange(1, tmp.size)
        val command = getCommand(identifier)
        if (command != null) {
            if (command.scope == Command.Scope.GUILD && event.guild == null ||
                    command.scope == Command.Scope.PRIVATE && event.guild != null)
                return
            val userPerms = event.author.botPermission(event.guild)

            logger.debug("Shard: ${event.message.shard.info[0]} Guild: ${event.guild?.stringID} " +
                    "Channel: ${event.channel.stringID} User: ${event.author.stringID} Command: " +
                    "\"${event.message.content}\"")

            val embed = EmbedBuilder().withColor(RED)
            val response = try {
                val builder = AdvancedMessageBuilder(event.channel)

                if (userPerms < command.botPerm)
                    null //insufficientPermission(event.channel, userPerms, command.botPerm)
                else if (event.guild != null &&
                        !event.author.getPermissionsForGuild(event.guild).containsAll(command.discordPerms))
                    insufficientPermission(
                            event.channel,
                            command.discordPerms
                                    .filter { !event.author.getPermissionsForGuild(event.guild).contains(it) }
                    )
                else {
                    event.client.dispatcher.dispatch(
                            CommandExecutedEvent(event.client,
                                    command,
                                    identifier,
                                    event.guild,
                                    event.channel,
                                    event.author
                            )
                    )
                    command.execute(identifier, params, event, builder)
                }
            } catch (e: MissingPermissionsException) {
                embed.withTitle("Missing Permissions!")
                embed.withDesc("I need the Discord permission ${e.message} to use that command!")
                AdvancedMessageBuilder(event.channel).withEmbed(embed)
            } catch (t: Throwable) {
                t.printStackTrace()
                embed.withTitle("An error occurred while executing that command!")
                var str = "${t.javaClass.name}: ${t.message}\n"
                var i = 0
                while (i < t.stackTrace.size && i < 6) {
                    str += "\tat ${t.stackTrace[i]}\n"
                    i++
                }
                if (t.stackTrace.size > 6)
                    str += "\t+ ${t.stackTrace.size-6} more"
                embed.withDesc(str)
                AdvancedMessageBuilder(event.channel).withEmbed(embed)
            }
            RequestBuffer.request {
                try {
                    response?.build()
                } catch (e: MissingPermissionsException) {
                    if (e.missingPermissions.contains(Permissions.EMBED_LINKS)) {
                        AdvancedMessageBuilder(event.channel).withContent("I use discord embeds for the " +
                                "majority of my features. In order to work properly, I need the **Embed Links** permission.").build()
                    }
                    e.printStackTrace()
                }
            }
        }
    }

    private fun insufficientPermission(channel: IChannel, perm: Permission, required: Permission): AdvancedMessageBuilder {
        val builder = AdvancedMessageBuilder(channel)
        val embed = EmbedBuilder().withColor(RED)
        builder.withContent("**You do not have the permissions required to use this command.** [Required:" +
                " **$required**, you have **$perm**]")
        return builder.withAutoDelete(30)
    }

    private fun insufficientPermission(channel: IChannel, discordPerms: List<Permissions>): AdvancedMessageBuilder {
        val builder = AdvancedMessageBuilder(channel)
        val embed = EmbedBuilder().withColor(RED)
        embed.withTitle("Permissions Error")
        embed.withDesc("**You do not have the permissions required to use this command.** [Required:" +
                " **DISCORD $discordPerms**]")
        return builder.withAutoDelete(30)
    }
}

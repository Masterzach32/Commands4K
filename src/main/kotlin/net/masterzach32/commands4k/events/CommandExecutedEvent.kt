package net.masterzach32.commands4k.events

import discord4j.core.DiscordClient
import discord4j.core.`object`.entity.Guild
import discord4j.core.`object`.entity.MessageChannel
import discord4j.core.`object`.entity.User
import net.masterzach32.commands4k.Command

class CommandExecutedEvent(
        client: DiscordClient,
        command: Command,
        val aliasUsed: String,
        val guild: Guild?,
        val channel: MessageChannel,
        val author: User
) : CommandEvent(client, command) {

    val isDM: Boolean = guild == null
}
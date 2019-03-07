package net.masterzach32.commands4k

import discord4j.core.`object`.entity.Guild
import discord4j.core.`object`.entity.MessageChannel
import discord4j.core.event.domain.message.MessageCreateEvent

class CommandContext(
        val event: MessageCreateEvent,
        val aliasUsed: String,
        val channel: MessageChannel,
        val guild: Guild,
        val args: Map<String, Argument>
) {

    val client = event.client
    val message = event.message
    val author = message.author
    val authorAsMember = message.authorAsMember
}
package net.masterzach32.commands4k

import discord4j.core.DiscordClient
import discord4j.core.`object`.entity.*
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono

class CommandContext(
        val event: MessageCreateEvent,
        val aliasUsed: String,
        val channel: MessageChannel,
        val guild: Guild,
        val args: Map<String, Argument>
) {

    val client: DiscordClient = event.client
    val message: Message = event.message
    val author: User? = message.author.orElse(null)
    val authorAsMember: Mono<Member> = message.authorAsMember
}
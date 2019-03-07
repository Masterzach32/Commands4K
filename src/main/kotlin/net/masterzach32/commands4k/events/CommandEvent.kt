package net.masterzach32.commands4k.events

import discord4j.core.DiscordClient
import discord4j.core.event.domain.Event
import net.masterzach32.commands4k.Command

open class CommandEvent(client: DiscordClient, val command: Command) : Event(client)
package net.masterzach32.commands4k

import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.util.*

/*
 * Command4K - Created on 8/25/17
 * Author: zachk
 * 
 * This code is licensed under the GNU GPL v3
 * You can find more info in the LICENSE file at the project root.
 */

/**
 * @author zachk
 * @version 8/25/17
 */
class AdvancedMessageBuilder(channel: IChannel) : MessageBuilder(channel.client) {

    var autoDeleteTime = 0

    init {
        withChannel(channel)
    }

    fun withAutoDelete(seconds: Int): AdvancedMessageBuilder {
        autoDeleteTime = seconds
        return this
    }

    override fun build(): IMessage {
        val message = super.build()
        if (autoDeleteTime > 0) {
            Thread {
                Thread.sleep((autoDeleteTime * 1000).toLong())
                RequestBuffer.request { message?.delete() }
            }.start()
        }
        return message
    }

    override fun withContent(content: String): AdvancedMessageBuilder {
        return super.withContent(content) as AdvancedMessageBuilder
    }

    fun withEmbed(embed: EmbedBuilder): AdvancedMessageBuilder {
        return super.withEmbed(embed.build()) as AdvancedMessageBuilder
    }
}
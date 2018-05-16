package net.masterzach32.commands4k.events

import net.masterzach32.commands4k.Command
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IUser

/*
 * Commands4K - Created on 5/15/2018
 * Author: Zach Kozar
 * 
 * This code is licensed under the GNU GPL v3
 * You can find more info in the LICENSE file at the project root.
 */

/**
 * @author Zach Kozar
 * @version 5/15/2018
 */
class CommandExecutedEvent(client: IDiscordClient, command: Command, val aliasUsed: String, val guild: IGuild?,
                           val channel: IChannel, val author: IUser) : CommandEvent(client, command) {

    fun inPrivateChannel(): Boolean = guild == null
}
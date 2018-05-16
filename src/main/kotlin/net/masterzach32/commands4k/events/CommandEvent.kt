package net.masterzach32.commands4k.events

import net.masterzach32.commands4k.Command
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.Event

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
abstract class CommandEvent(client: IDiscordClient, val command: Command) : Event() {

    init {
        this.client = client
    }
}
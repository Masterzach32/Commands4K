package net.masterzach32.commands4k

import discord4j.core.`object`.entity.Guild
import discord4j.core.`object`.entity.User
import discord4j.core.`object`.util.Permission
import discord4j.core.event.EventDispatcher
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.spec.MessageCreateSpec
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.function.Consumer

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
@Suppress("LABEL_NAME_CLASH")
class CommandListener(
        dispatcher: EventDispatcher,
        private val commandPrefix: (Guild?) -> String,
        private val botPermission: User.(Guild?) -> BotPermissions
) : CommandManager(dispatcher), Consumer<MessageCreateEvent> {

    private val logger = LoggerFactory.getLogger("Commands4K")

    init {
        dispatcher.on(MessageCreateEvent::class).subscribe(this)
    }

    override fun accept(event: MessageCreateEvent) {
        val message = event.message

        Mono.justOrEmpty(event.message.content)
                .filter { message.author.orElse(null)?.isBot?.not() ?: false }
                .map { it.split(" ") }
                .flatMap { content ->
                    message.channel.flatMap { channel ->
                        event.guild.flatMap { guild ->
                            Mono.just(commandPrefix(guild)).flatMap { commandPrefix ->
                                Flux.fromIterable(getCommandList())
                                        .filter { content.first().startsWith(commandPrefix) }
                                        .filter { it.aliases.contains(content.first().drop(1)) }
                                        .flatMap { cmd ->
                                            val postArgs = mutableMapOf<String, Argument>()
                                            try {
                                                if (cmd.processArgs) {
                                                    val preArgs = content.drop(1)

                                                    val requiredArgs = cmd.args.filter { it.required }.size
                                                    if (preArgs.size < requiredArgs)
                                                        return@flatMap channel.createMessage { invalidArgsResponse(it, cmd) }


                                                    for (i in cmd.args.indices) {
                                                        if (cmd.args[i].infinite) {
                                                            val tmp = mutableListOf<Any>()
                                                            for (j in i until preArgs.size)
                                                                tmp.add(cmd.args[i].parser.invoke(preArgs[j]))
                                                            postArgs[cmd.args[i].key] = Argument(tmp, cmd.args[i])
                                                        } else if (!cmd.args[i].required) {
                                                            if (preArgs.size > i)
                                                                postArgs[cmd.args[i].key] = Argument(
                                                                        cmd.args[i].parser.invoke(preArgs[i]),
                                                                        cmd.args[i]
                                                                )
                                                        } else
                                                            postArgs[cmd.args[i].key] = Argument(
                                                                    cmd.args[i].parser.invoke(preArgs[i]),
                                                                    cmd.args[i]
                                                            )
                                                    }
                                                } else {
                                                    postArgs[cmd.args[0].key] = Argument(
                                                            message.content.get().drop(message.content.get().indexOf(" ") + 1),
                                                            cmd.args[0]
                                                    )
                                                }
                                            } catch (e: Exception) {
                                                return@flatMap channel.createMessage { parseErrorResponse(it, cmd, e) }
                                            }
                                            try {
                                                cmd.execute(CommandContext(event, content.first().drop(1), channel, guild, postArgs))
                                            } catch (e: Exception) {
                                                channel.createMessage { commandExceptionResponse(it, cmd, e) }
                                            }
                                        }.next()
                            }
                        }
                    }
                }.subscribe()
    }

    private fun invalidArgsResponse(msgSpec: MessageCreateSpec, command: Command) {
        msgSpec.setEmbed {
            it.setDescription("invalid args for ${command.name}")
            it.setColor(RED)
        }
    }

    private fun parseErrorResponse(msgSpec: MessageCreateSpec, command: Command, e: Exception) {
        msgSpec.setEmbed {
            when (e) {
                is NumberFormatException -> it.setDescription("Could not parse a number in the arguments given. " +
                        "Perhaps you have an invalid character in your args, or the number is too large")
                else -> it.setDescription("An unknown error occurred: $e")
            }
            it.setColor(RED)
        }
    }

    private fun commandExceptionResponse(msgSpec: MessageCreateSpec, command: Command, e: Exception) {
        msgSpec.setEmbed {
            it.setDescription("Command ${command.name} threw an error on execution: $e")
            it.setColor(RED)
        }
    }
}

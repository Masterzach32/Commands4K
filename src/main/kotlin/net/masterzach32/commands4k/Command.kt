package net.masterzach32.commands4k

import discord4j.core.`object`.util.Permission
import reactor.core.publisher.Mono

open class Command (
        /**
         * The name of the command.
         */
        val name: String,
        /**
         * Names that may be used to invoke the command.
         */
        val aliases: List<String>,
        /**
         * Short description of the command.
         */
        val description: String,
        /**
         * Arguments for the command.
         */
        val args: List<ArgumentInfo<Any>>,
        /**
         * A detailed description of the command and it's functionality
         */
        val details: String?,
        /**
         * Whether the command should be hidden from the help command.
         */
        val hidden: Boolean = false,
        /**
         * Whether the command context should parse the entire command as one arg or split
         * by spaces. If this is false only one arg can passed to args
         */
        val processArgs: Boolean = true,
        /**
         * Defines where this command can be used (GUILD, DM, BOTH)
         */
        val scope: Scope = Scope.ALL,
        /**
         * The permission level a user must have to invoke this command.
         */
        val botPerm: BotPermissions = BotPermissions.NORMAL,
        /**
         * List of discord perms the user must have to invoke this command.
         */
        val discordPerms: List<Permission> = listOf(),
        private val execFunc: CommandInvokeHandler = DEFAULT_EXEC
) {

    init {
        if(aliases.isEmpty())
            throw IllegalArgumentException("$name must have at least one alias!")
    }

    fun execute(cc: CommandContext): Mono<Void> {
        return execFunc.invoke(cc)
    }

    fun matchesAlias(cmd: String): Boolean {
        return aliases.any { it == cmd.toLowerCase() }
    }

    override fun toString(): String {
        return "$name - $aliases\nMinimum Permission: $botPerm"
    }

    enum class Scope {
        ALL, GUILD, PRIVATE
    }
}
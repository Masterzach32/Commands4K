package net.masterzach32.commands4k

import sx.blah.discord.handle.obj.*
import sx.blah.discord.util.*
import java.awt.Color
import java.util.*

fun getWrongArgumentsMessage(channel: IChannel, cmd: Command, cmdUsed: String): AdvancedMessageBuilder {
    val builder = AdvancedMessageBuilder(channel)
    builder.withContent("Incorrect number of arguments. Use `~help $cmdUsed` for details with this command.\n")
    builder.appendContent("Acceptable parameter types:")
    val map = LinkedHashMap<String, String>()
    cmd.getCommandHelp(map)
    map.forEach { k, v -> builder.appendContent("\n`$k` $v") }
    return builder.withAutoDelete(30)
}

fun getBotLockedMessage(channel: IChannel): AdvancedMessageBuilder {
    return AdvancedMessageBuilder(channel).withContent("**The bot is currently locked.**")
}

enum class Type { GET, POST }

fun getApiErrorMessage(channel: IChannel, type: Type, apiCall: String, body: String, status: Int, statusText: String): AdvancedMessageBuilder {
    val embed = EmbedBuilder().withColor(Color(244, 78, 66))
    embed.withTitle(":warning: The following API call returned a bad status code:")
    embed.withDesc("Error Message: $statusText\n$body")
    embed.appendField("API Call", apiCall, true)
    embed.appendField("Type", type.name, true)
    embed.appendField("Response Code", "$status", true)
    embed.withFooterText("Report this to the dev: https://github.com/Masterzach32/SwagBot")
    embed.withFooterIcon(channel.client.getUserByID("97341976214511616".toLong()).avatarURL)
    return AdvancedMessageBuilder(channel).withEmbed(embed)
}

fun waitAndDeleteMessage(message: IMessage?, seconds: Int) {
    Thread {
        RequestBuffer.request {
            try {
                Thread.sleep((seconds * 1000).toLong())
                message?.delete()
            } catch (e: MissingPermissionsException) {
                e.printStackTrace()
            } catch (e: DiscordException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }.start()
}

fun editMessage(message: IMessage?, contents: String): IMessage? {
    return RequestBuffer.request<IMessage?> {
        try {
            return@request message?.edit(contents)
        } catch (e: DiscordException) {
            e.printStackTrace()
        } catch (e: MissingPermissionsException) {
            e.printStackTrace()
        }
        return@request null
    }.get()
}
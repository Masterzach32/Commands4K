package net.masterzach32.commands4k.builder

import net.masterzach32.commands4k.AdvancedMessageBuilder
import sx.blah.discord.api.events.Event
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

val DEFAULT_EXEC: EventHandler = { null }

class EventBuilder : Builder<EventHandler> {

    private var noArgsFunc = DEFAULT_EXEC
    private var privateFunc = DEFAULT_EXEC
    private var guildFunc = DEFAULT_EXEC
    private var allFunc = DEFAULT_EXEC

    val listeners = mutableListOf<IListener<Event>>()

    fun noArgs(func: EventHandler) {
        noArgsFunc = func
    }

    fun private(func: EventHandler) {
        privateFunc = func
    }

    fun guild(func: EventHandler) {
        guildFunc = func
    }

    fun all(func: EventHandler) {
        allFunc = func
    }

    inline fun <reified E : Event> listen(crossinline func: (event: E) -> Unit) {
        listeners.add(IListener { if (it is E) func.invoke(it) })
    }

    override fun build(): EventHandler {
        return {
            if (args.isEmpty() && noArgsFunc != DEFAULT_EXEC)
                noArgsFunc.invoke(this)
            else if (allFunc != DEFAULT_EXEC)
                allFunc.invoke(this)
            else if (event.guild == null)
                privateFunc.invoke(this)
            else
                guildFunc.invoke(this)

        }
    }
}

class EventWrapper(val cmdUsed: String, val args: Array<String>, val event: MessageReceivedEvent,
                   val builder: AdvancedMessageBuilder)

typealias EventHandler = EventWrapper.() -> AdvancedMessageBuilder?
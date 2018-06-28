package net.masterzach32.commands4k.builder

import net.masterzach32.commands4k.AdvancedMessageBuilder
import sx.blah.discord.api.events.Event
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

val DEFAULT_BEHAVIOR: EventHandler = { null }

class EventBuilder : Builder<EventHandler> {

    private var noArgsBehavior = DEFAULT_BEHAVIOR
    private var privateBehavior = DEFAULT_BEHAVIOR
    private var guildBehavior = DEFAULT_BEHAVIOR
    private var allBehavior = DEFAULT_BEHAVIOR

    val listeners = mutableListOf<IListener<Event>>()

    fun noArgs(func: EventHandler) {
        noArgsBehavior = func
    }

    fun private(func: EventHandler) {
        privateBehavior = func
    }

    fun guild(func: EventHandler) {
        guildBehavior = func
    }

    fun all(func: EventHandler) {
        allBehavior = func
    }

    inline fun <reified E : Event> listen(crossinline listener: E.() -> Unit) {
        listeners.add(IListener { (it as? E)?.listener() })
    }

    override fun build(): EventHandler {
        return {
            if (args.isEmpty() && noArgsBehavior != DEFAULT_BEHAVIOR)
                noArgsBehavior.invoke(this)
            else if (allBehavior != DEFAULT_BEHAVIOR)
                allBehavior.invoke(this)
            else if (event.guild == null)
                privateBehavior.invoke(this)
            else
                guildBehavior.invoke(this)

        }
    }
}

class EventWrapper(
        val cmdUsed: String,
        val args: Array<String>,
        val event: MessageReceivedEvent,
        val builder: AdvancedMessageBuilder
)

typealias EventHandler = EventWrapper.() -> AdvancedMessageBuilder?
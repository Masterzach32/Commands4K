package net.masterzach32.commands4k

import discord4j.core.event.EventDispatcher
import discord4j.core.event.domain.Event
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.awt.Color
import kotlin.reflect.KClass

/*
 * Commands4K - Created on 11/14/2017
 * Author: Zach Kozar
 * 
 * This code is licensed under the GNU GPL v3
 * You can find more info in the LICENSE file at the project root.
 */

/**
 * @author Zach Kozar
 * @version 11/14/2017
 */

// embed colors
val RED = Color(244, 78, 66)
val GREY = Color(200, 200, 200)

typealias Parser<A> = (String) -> A
typealias Validator<A> = (A) -> Boolean
typealias EmptyChecker<A> = (A) -> Boolean
typealias ErrorHandler = (String, Exception) -> Unit

fun <E : Event> EventDispatcher.on(event: KClass<E>): Flux<E> = on(event.java)

typealias CommandInvokeHandler = CommandContext.() -> Mono<Void>

val DEFAULT_EXEC: CommandInvokeHandler = { Mono.just(println("The command invoked by $aliasUsed hasn't been implemented yet!")).then() }
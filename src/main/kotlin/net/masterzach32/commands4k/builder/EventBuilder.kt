package net.masterzach32.commands4k.builder

import net.masterzach32.commands4k.CommandInvokeHandler

class EventBuilder : Builder<CommandInvokeHandler> {

    lateinit var exec: CommandInvokeHandler

    fun run(func: CommandInvokeHandler) {
        exec = func
    }

    override fun build(): CommandInvokeHandler {
        return exec
    }
}

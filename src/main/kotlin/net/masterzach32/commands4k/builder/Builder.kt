package net.masterzach32.commands4k.builder

interface Builder<out T> {

    fun build(): T
}
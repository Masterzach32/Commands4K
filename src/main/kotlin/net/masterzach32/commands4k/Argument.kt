package net.masterzach32.commands4k

class Argument(
        /**
         * The value of the argument.
         */
        val value: Any,
        /**
         * The ArgumentInfo object associated with this argument.
         */
        val info: ArgumentInfo<Any>
) {

    //constructor(input: String, info: ArgumentInfo<Any>) : this(info.parser.invoke(input), info)

    @Suppress("UNCHECKED_CAST")
    fun <V> valueAs(): V = value as V

    @Suppress("UNCHECKED_CAST")
    fun <I : Any> infoAs(): ArgumentInfo<I> = info as ArgumentInfo<I>

    override fun toString(): String = value.toString()
}
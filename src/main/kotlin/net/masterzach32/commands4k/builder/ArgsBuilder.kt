package net.masterzach32.commands4k.builder

import net.masterzach32.commands4k.*
import kotlin.math.roundToInt
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class ArgsBuilder : Builder<List<ArgumentInfo<Any>>> {

    val args = mutableListOf<ArgumentInfo<*>>()

    inline fun <reified A : Any> arg(func: ArgsInfoBuilder<A>.() -> Unit) {
        args.add(ArgsInfoBuilder<A>().apply { type = A::class }.apply(func).build())
    }

    override fun build(): List<ArgumentInfo<Any>> {
        return args.toList() as List<ArgumentInfo<Any>>
    }

    class ArgsInfoBuilder<A : Any> : Builder<ArgumentInfo<A>> {

        var key: String? = null
        lateinit var type: KClass<A>
        var label: String? = null
        var required: Boolean = true
        var infinite: Boolean = false
        var max: Int? = null
        var min: Int? = null
        var parser: Parser<out A>? = null
        var validator: Validator<in A>? = null
        var emptyChecker: EmptyChecker<in A>? = null
        var errorHandler: ErrorHandler? = null
        var default: A? = null

        init {
            parser = when (type) {
                String::class -> STRING_PARSER
                Double::class -> DECIMAL_PARSER
                Int::class -> INTEGER_PARSER
                else -> null
            } as Parser<out A>
        }

        override fun build(): ArgumentInfo<A> {
            if (key == null)
                throw IllegalArgumentException("Argument key cannot be null!")
            if (type == null)
                throw IllegalArgumentException("Argument type cannot be null! (this shouldn't happen)")
            if (parser == null)
                throw IllegalArgumentException("Argument parser cannot be null!")
            if (label == null)
                label = key!!
            if (default != null)
                required = false

            if (min != null || max != null) {
                validator = {
                    var valid = true
                    val _min = min
                    val _max = max
                    if (_min != null) {
                        valid = when (it) {
                            is String -> it.length > _min
                            is Number -> it.toDouble() > _min
                            else -> true
                        }
                    }
                    if (_max != null) {
                        valid = when (it) {
                            is String -> it.length < _max
                            is Number -> it.toDouble() < _max
                            else -> true
                        }
                    }
                    valid && validator?.invoke(it) ?: true
                }
            }

            return ArgumentInfo(key!!, type, label!!, required, infinite, max, min, parser!!, validator, emptyChecker, errorHandler, default)
        }
    }

    companion object {
        val STRING_PARSER: Parser<String> = { it }
        val DECIMAL_PARSER: Parser<Double> = { it.toDouble() }
        val INTEGER_PARSER: Parser<Int> = { it.toDouble().roundToInt() }
    }
}
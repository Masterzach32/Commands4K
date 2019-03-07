package net.masterzach32.commands4k

import kotlin.reflect.KClass

data class ArgumentInfo<A : Any>(
        /**
         * Key for the argument.
         */
        val key: String,
        /**
         * Type (class) of the argument.
         */
        val type: KClass<A>,
        /**
         * User-friendly label for the argument.
         */
        val label: String,
        /**
         * Whether the argument is required by the command.
         */
        val required: Boolean,
        /**
         * Whether the argument accepts an infinite number of values or just one.
         */
        val infinite: Boolean,
        /**
         * If the type is Number, this is the max value of the number.
         * If the type is String, this is the max length of the String.
         */
        val max: Int?,
        /**
         * If the type is Number, this is the min value of the number.
         * If the type is String, this is the min length of the String.
         */
        val min: Int?,
        /**
         * Parser function for parsing the value for the argument from a string.
         * Defaults are provided for String, Int and Double.
         */
        val parser: Parser<out A>,
        /**
         * Validator function for checking whether the value for the argument is valid.
         *
         * This function is not implemented in code yet, so it will not do anything.
         */
        val validator: Validator<in A>?,
        /**
         * Function to check whether the argument is empty.
         *
         * This function is not implemented in code yet, so it will not do anything.
         */
        val emptyChecker: EmptyChecker<in A>?,
        /**
         * Function to handle errors when parsing an argument fails.
         *
         * This function is not implemented in code yet, so it will not do anything.
         */
        val errorHandler: ErrorHandler?,
        /**
         * The default value for the argument.
         */
        val default: A?
)
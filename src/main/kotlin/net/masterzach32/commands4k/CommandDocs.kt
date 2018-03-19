package net.masterzach32.commands4k

class CommandDocs {

    var desc: String? = null
    val usage = mutableMapOf<String, String>()

    fun hasDesc(): Boolean {
        return desc != null
    }

    fun hasUsage(): Boolean {
        return usage.isNotEmpty()
    }

    fun hasHelpText(): Boolean {
        return hasDesc() || hasUsage()
    }

}

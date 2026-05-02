package com.shishusneh.app.utils

import java.security.MessageDigest

object PasswordUtils {
    fun hash(value: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(value.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}

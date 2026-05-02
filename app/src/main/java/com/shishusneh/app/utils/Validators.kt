package com.shishusneh.app.utils

object Validators {
    fun isValidEmail(email: String): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()

    fun isStrongPassword(password: String): Boolean = password.length >= 6

    fun validateWeight(weight: String): String? {
        val value = weight.toDoubleOrNull() ?: return "Enter a valid weight"
        return if (value in 1.0..20.0) null else "Weight should be between 1 and 20 kg"
    }

    fun validateHeight(height: String): String? {
        val value = height.toDoubleOrNull() ?: return "Enter a valid height"
        return if (value in 20.0..120.0) null else "Height should be between 20 and 120 cm"
    }
}

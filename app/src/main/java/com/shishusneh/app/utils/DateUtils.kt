package com.shishusneh.app.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateUtils {
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    fun formatDate(millis: Long): String = dateFormat.format(Date(millis))

    fun ageInWeeks(dobMillis: Long): Int {
        val diff = System.currentTimeMillis() - dobMillis
        return TimeUnit.MILLISECONDS.toDays(diff).toInt() / 7
    }

    fun ageInMonths(dobMillis: Long): Int {
        val now = Calendar.getInstance()
        val dob = Calendar.getInstance().apply { timeInMillis = dobMillis }
        var months = (now.get(Calendar.YEAR) - dob.get(Calendar.YEAR)) * 12
        months += now.get(Calendar.MONTH) - dob.get(Calendar.MONTH)
        if (now.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) months--
        return months.coerceAtLeast(0)
    }

    fun ageLabel(dobMillis: Long): String {
        val months = ageInMonths(dobMillis)
        val weeks = ageInWeeks(dobMillis)
        return if (months < 1) "$weeks weeks" else "$months months"
    }

    fun addDays(baseMillis: Long, days: Int): Long = baseMillis + TimeUnit.DAYS.toMillis(days.toLong())

    fun addMonths(baseMillis: Long, months: Int): Long {
        return Calendar.getInstance().apply {
            timeInMillis = baseMillis
            add(Calendar.MONTH, months)
        }.timeInMillis
    }

    fun monthsBetween(startMillis: Long, endMillis: Long): Int {
        val start = Calendar.getInstance().apply { timeInMillis = startMillis }
        val end = Calendar.getInstance().apply { timeInMillis = endMillis }
        var months = (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12
        months += end.get(Calendar.MONTH) - start.get(Calendar.MONTH)
        if (end.get(Calendar.DAY_OF_MONTH) < start.get(Calendar.DAY_OF_MONTH)) months--
        return months.coerceAtLeast(0)
    }
}

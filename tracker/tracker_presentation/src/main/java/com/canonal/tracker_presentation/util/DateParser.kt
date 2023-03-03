package com.canonal.tracker_presentation.util

import android.content.Context
import com.canonal.core.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun parseDate(date: LocalDate, context: Context): String {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)
    val tomorrow = today.plusDays(1)
    return when (date) {
        today -> context.getString(R.string.today)
        yesterday -> context.getString(R.string.yesterday)
        tomorrow -> context.getString(R.string.tomorrow)
        else -> DateTimeFormatter.ofPattern("dd LLLL").format(date)
    }
}

package ru.dw.astronomypictureoftheday.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat", "WeekBasedYear")
fun getCurrentDays(): String {
    val sdf = SimpleDateFormat("YYYY-MM-dd")
    val calendar = Calendar.getInstance()
    return sdf.format(calendar.time)
}

@SuppressLint("SimpleDateFormat", "WeekBasedYear")
fun convertDateFormat(date: String): String {
    val inputFormat = android.icu.text.SimpleDateFormat("MMM d, yyyy")
    val parseDate = inputFormat.parse(date)
    val outputFormat = SimpleDateFormat("YYYY-MM-dd")
    return outputFormat.format(parseDate.time)
}

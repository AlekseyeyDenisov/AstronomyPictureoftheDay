package ru.dw.astronomypictureoftheday.utils

import android.annotation.SuppressLint
import ru.dw.astronomypictureoftheday.data.room.DayPhotoEntity
import ru.dw.astronomypictureoftheday.model.DayPhotoResponse
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

fun convertSuccessesToEntity(dayPhotoResponse: DayPhotoResponse):DayPhotoEntity{
    return DayPhotoEntity(
        0,
        dayPhotoResponse.date,
        dayPhotoResponse.hdUrl?:EMPTY_STRING ,
        dayPhotoResponse.explanation?:EMPTY_STRING,
        dayPhotoResponse.title?:EMPTY_STRING,
        dayPhotoResponse.url?:EMPTY_STRING,
        dayPhotoResponse.mediaType
    )
}

const val EMPTY_STRING = "empty"
const val CONSTANT_VIDEO = "video"
const val CONSTANT_IMAGE = "image"
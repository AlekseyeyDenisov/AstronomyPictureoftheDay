package ru.dw.astronomypictureoftheday.utils

import android.annotation.SuppressLint
import ru.dw.astronomypictureoftheday.data.room.DayPhotoEntity
import ru.dw.astronomypictureoftheday.model.DayPhotoResponse
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat", "WeekBasedYear")
fun getCurrentDays(): String {
    val sdf = SimpleDateFormat(CONSTANT_FORMAT_DATE)
    val calendar = Calendar.getInstance()
    return sdf.format(calendar.time)
}

@SuppressLint("SimpleDateFormat", "WeekBasedYear")
fun convertDateFormat(date: Long): String {
    val outputFormat = SimpleDateFormat(CONSTANT_FORMAT_DATE)
    return outputFormat.format(date)
}

fun convertSuccessesToEntity(dayPhotoResponse: DayPhotoResponse):DayPhotoEntity{
    return DayPhotoEntity(
        0,
        dayPhotoResponse.date,
        dayPhotoResponse.hdUrl,
        dayPhotoResponse.explanation,
        dayPhotoResponse.title,
        dayPhotoResponse.url,
        dayPhotoResponse.mediaType
    )
}

const val EMPTY_STRING = "empty"
const val CONSTANT_VIDEO = "video"
const val CONSTANT_IMAGE = "image"
const val CONSTANT_FORMAT_DATE = "YYYY-MM-dd"
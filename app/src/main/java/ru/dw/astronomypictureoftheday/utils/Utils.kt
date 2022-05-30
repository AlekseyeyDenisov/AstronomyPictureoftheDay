package ru.dw.astronomypictureoftheday.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import ru.dw.astronomypictureoftheday.data.room.DayPhotoEntity
import ru.dw.astronomypictureoftheday.model.DayPhotoResponse
import java.text.SimpleDateFormat
import java.util.*

const val EMPTY_STRING = "empty"
const val CONSTANT_VIDEO = "video"
const val CONSTANT_IMAGE = "image"
const val CONSTANT_FORMAT_DATE = "YYYY-MM-dd"

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
        dayPhotoResponse.date?:EMPTY_STRING,
        dayPhotoResponse.hdUrl?:EMPTY_STRING,
        dayPhotoResponse.explanation?:EMPTY_STRING,
        dayPhotoResponse.title?:EMPTY_STRING,
        dayPhotoResponse.url?:EMPTY_STRING,
        dayPhotoResponse.mediaType?:EMPTY_STRING
    )
}

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.d("@@@", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.d("@@@", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.d("@@@", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}


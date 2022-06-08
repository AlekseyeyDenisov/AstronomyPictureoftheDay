package ru.dw.astronomypictureoftheday.utils

import android.content.Context
import android.content.SharedPreferences

private const val CONSTANT_SHARED_PREFERENCES = "weather_sharedPreferences"
private const val SHARED_PREFERENCES_IS_INTERNET = "sharedPreferences_floating_is_internet"


class SharedPreferencesManager(context: Context) {
    private var pref: SharedPreferences =
        context.getSharedPreferences(CONSTANT_SHARED_PREFERENCES, Context.MODE_PRIVATE)



    fun getIsInternet(): Boolean {
        return pref.getBoolean(SHARED_PREFERENCES_IS_INTERNET, true)
    }

    fun setIsInternet(flag: Boolean) {
        pref.edit().putBoolean(SHARED_PREFERENCES_IS_INTERNET, flag).apply()
    }

}
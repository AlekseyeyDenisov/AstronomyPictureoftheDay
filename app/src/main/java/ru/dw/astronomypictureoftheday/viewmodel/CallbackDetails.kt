package ru.dw.astronomypictureoftheday.viewmodel


import ru.dw.astronomypictureoftheday.model.DayPhotoResponse

interface CallbackDetails {
    fun onResponseSuccess(successes: List<DayPhotoResponse>)
    fun onFail(error: String)
}
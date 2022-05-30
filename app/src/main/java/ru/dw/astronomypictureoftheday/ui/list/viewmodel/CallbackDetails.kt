package ru.dw.astronomypictureoftheday.ui.list.viewmodel


import ru.dw.astronomypictureoftheday.model.DayPhotoResponse

interface CallbackDetails {
    fun onResponseSuccess(successes: List<DayPhotoResponse>)
    fun onFail(error: String)
}
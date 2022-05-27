package ru.dw.astronomypictureoftheday.ui.list


import ru.dw.astronomypictureoftheday.model.DayPhotoResponse

sealed class PictureAppState{
    data class Success(val dayPhotoResponse: List<DayPhotoResponse>) : PictureAppState()
    data class Error(val error: String) : PictureAppState()
    object Loading : PictureAppState()
}

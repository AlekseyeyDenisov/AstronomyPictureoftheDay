package ru.dw.astronomypictureoftheday.ui.list


sealed class PictureAppState{
    object Success: PictureAppState()
    data class Error(val error: String) : PictureAppState()
    object Loading : PictureAppState()
}

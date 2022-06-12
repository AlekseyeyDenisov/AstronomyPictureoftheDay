package ru.dw.astronomypictureoftheday.viewmodel

import ru.dw.astronomypictureoftheday.data.room.DayPhotoEntity


sealed class PictureAppState{
    data class Success(val listPhoto: List<DayPhotoEntity>): PictureAppState()
    data class Error(val error: String) : PictureAppState()
    object Loading : PictureAppState()
}

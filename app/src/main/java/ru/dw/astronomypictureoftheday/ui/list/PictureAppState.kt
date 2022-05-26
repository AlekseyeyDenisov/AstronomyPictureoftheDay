package ru.dw.astronomypictureoftheday.ui.list

import ru.dw.astronomypictureoftheday.domen.ResponseDataItemDay

sealed class PictureAppState{
    data class Success(val responseDataItemDay: List<ResponseDataItemDay>) : PictureAppState()
    data class Error(val error: String) : PictureAppState()
    object Loading : PictureAppState()
}

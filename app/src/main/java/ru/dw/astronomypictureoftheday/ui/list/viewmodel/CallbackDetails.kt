package ru.dw.astronomypictureoftheday.ui.list.viewmodel

import ru.dw.astronomypictureoftheday.domen.ResponseDataItemDay

interface CallbackDetails {
    fun onResponseSuccess(success: List<ResponseDataItemDay>)
    fun onFail(error: String)
}
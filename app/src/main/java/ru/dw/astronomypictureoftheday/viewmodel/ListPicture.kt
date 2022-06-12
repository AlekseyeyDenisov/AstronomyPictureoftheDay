package ru.dw.astronomypictureoftheday.viewmodel

interface ListPicture {
    fun getListDayPicture(whatDay: String,callbackDetails: CallbackDetails)
}
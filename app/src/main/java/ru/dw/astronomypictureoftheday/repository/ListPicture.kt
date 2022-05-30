package ru.dw.astronomypictureoftheday.repository

import ru.dw.astronomypictureoftheday.ui.list.viewmodel.CallbackDetails

interface ListPicture {
    fun getListDayPicture(whatDay: String,callbackDetails: CallbackDetails)
}
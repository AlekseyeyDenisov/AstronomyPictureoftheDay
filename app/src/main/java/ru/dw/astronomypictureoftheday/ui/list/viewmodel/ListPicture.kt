package ru.dw.astronomypictureoftheday.ui.list.viewmodel

import ru.dw.astronomypictureoftheday.ui.list.viewmodel.CallbackDetails

interface ListPicture {
    fun getListDayPicture(whatDay: String,callbackDetails: CallbackDetails)
}
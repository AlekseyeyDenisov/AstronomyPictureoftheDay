package ru.dw.astronomypictureoftheday.ui.list.viewmodel

import ru.dw.astronomypictureoftheday.repository.ListPicture

interface Repository {
    fun getDataList(): ListPicture
}
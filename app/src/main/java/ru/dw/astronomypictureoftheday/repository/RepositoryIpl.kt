package ru.dw.astronomypictureoftheday.repository

import ru.dw.astronomypictureoftheday.data.RetrofitHelper
import ru.dw.astronomypictureoftheday.ui.list.viewmodel.Repository

object RepositoryIpl: Repository {
    private val dataApiNasa: ListPicture = RetrofitHelper


    override fun getDataList():ListPicture {
        //TODO Сделать проверку на подкючению к интернету и вернуть Room или Retrofit
        return  dataApiNasa

    }


}
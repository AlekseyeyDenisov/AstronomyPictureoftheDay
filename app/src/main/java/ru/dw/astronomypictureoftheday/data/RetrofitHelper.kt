package ru.dw.astronomypictureoftheday.data

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    private val retrofit: Retrofit = initRetrofit()
    private  val nasaBaseUrl = "https://api.nasa.gov/"

    private fun initRetrofit(): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(nasaBaseUrl)
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        }.build()
    }

}
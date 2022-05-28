package ru.dw.astronomypictureoftheday.data.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.dw.astronomypictureoftheday.model.DayPhotoResponse

interface RetrofitApi {
    @GET("planetary/apod")
    fun getPictureOfTheDay(
        @Query("api_key") apiKey: String,
        @Query("start_date") startData: String,
        @Query("end_date") endData: String
    ): Call<List<DayPhotoResponse>>
}
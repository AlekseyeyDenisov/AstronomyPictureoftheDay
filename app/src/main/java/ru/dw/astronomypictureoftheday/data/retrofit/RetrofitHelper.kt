package ru.dw.astronomypictureoftheday.data.retrofit

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.dw.astronomypictureoftheday.BuildConfig
import ru.dw.astronomypictureoftheday.model.DayPhotoResponse
import ru.dw.astronomypictureoftheday.ui.list.viewmodel.CallbackDetails
import ru.dw.astronomypictureoftheday.ui.list.viewmodel.ListPicture

object RetrofitHelper: ListPicture {
    private val retrofit: Retrofit = initRetrofit()
    private const val BASE_URL_NASA = "https://api.nasa.gov/"

    private fun initRetrofit(): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(BASE_URL_NASA)
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        }.build()
    }

    override fun getListDayPicture(whatDay: String,callbackDetails: CallbackDetails) {
        retrofit
            .create(RetrofitApi::class.java)
            .getPictureOfTheDay(BuildConfig.NASA_API_KEY, whatDay, whatDay)
            .enqueue(
                object : Callback<List<DayPhotoResponse>> {
                    override fun onResponse(
                        call: Call<List<DayPhotoResponse>>,
                        responseListPhotoResponse: Response<List<DayPhotoResponse>>
                    ) {
                        responseListPhotoResponse.body()
                        if (responseListPhotoResponse.isSuccessful) {
                            responseListPhotoResponse.body()?.let {
                                callbackDetails.onResponseSuccess(it)
                            }
                        } else {
                            callbackDetails.onFail("Error code: ${responseListPhotoResponse.code()}")
                        }
                    }

                    override fun onFailure(
                        call: Call<List<DayPhotoResponse>>,
                        t: Throwable
                    ) {
                        t.message?.let {error->
                            callbackDetails.onFail(error)
                        }

                    }

                })

    }

}
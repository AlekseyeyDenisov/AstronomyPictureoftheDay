package ru.dw.astronomypictureoftheday.ui.list.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dw.astronomypictureoftheday.model.DayPhotoResponse
import ru.dw.astronomypictureoftheday.repository.RepositoryIpl
import ru.dw.astronomypictureoftheday.ui.list.PictureAppState


class ListPhotosViewModel(
    private val repository: Repository = RepositoryIpl,
    private val liveData: MutableLiveData<PictureAppState> = MutableLiveData()
) : ViewModel() {

    fun getLiveData(): MutableLiveData<PictureAppState> {
        return liveData
    }

    fun sendRequest(date:String){
        liveData.postValue(PictureAppState.Loading)
        repository.getDataList().getListDayPicture(date,object :CallbackDetails{
            override fun onResponseSuccess(successes: List<DayPhotoResponse>) {
                liveData.postValue(PictureAppState.Success(successes))
                successes.forEach{
                    Log.d("@@@", "onResponseSuccess: ${it.title}")
                }

            }

            override fun onFail(error: String) {
                Log.d("@@@", "onFail: $error")
            }

        })
    }

}
package ru.dw.astronomypictureoftheday.ui.list.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dw.astronomypictureoftheday.MyApp
import ru.dw.astronomypictureoftheday.model.DayPhotoResponse
import ru.dw.astronomypictureoftheday.repository.RepositoryIpl
import ru.dw.astronomypictureoftheday.ui.list.PictureAppState
import ru.dw.astronomypictureoftheday.utils.convertSuccessesToEntity


class ListPhotosViewModel(
    private val repository: Repository = RepositoryIpl,
    private val liveData: MutableLiveData<PictureAppState> = MutableLiveData()
) : ViewModel() {

    val helperRoom = MyApp.getDBRoom()

    fun getLiveData(): MutableLiveData<PictureAppState> {
        return liveData
    }

    fun sendRequest(date: String) {
        liveData.postValue(PictureAppState.Loading)
        repository.getDataList().getListDayPicture(date, object : CallbackDetails {
            override fun onResponseSuccess(successes: List<DayPhotoResponse>) {
                Log.d("@@@", "onResponseSuccess: ${successes[0]}")
                Thread {
                    try {
                        val entity = convertSuccessesToEntity(successes[0])
                        helperRoom.setDayPhoto(entity)
                    } catch (e: NullPointerException) {
                        e.message?.let {
                            liveData.postValue(PictureAppState.Error(e.message!!))
                        }
                    }
                }.start()
                liveData.postValue(PictureAppState.Success)
            }

            override fun onFail(error: String) {
                liveData.postValue(PictureAppState.Error(error))
                Log.d("@@@", "onFail: $error")
            }

        })
    }

}
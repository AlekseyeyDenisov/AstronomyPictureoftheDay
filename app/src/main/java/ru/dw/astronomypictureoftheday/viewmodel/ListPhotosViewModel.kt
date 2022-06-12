package ru.dw.astronomypictureoftheday.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.dw.astronomypictureoftheday.MyApp
import ru.dw.astronomypictureoftheday.data.FileHelper
import ru.dw.astronomypictureoftheday.data.retrofit.RetrofitHelper
import ru.dw.astronomypictureoftheday.data.room.DayPhotoEntity
import ru.dw.astronomypictureoftheday.model.DayPhotoResponse
import ru.dw.astronomypictureoftheday.utils.CONSTANT_IMAGE
import ru.dw.astronomypictureoftheday.utils.CONSTANT_IMAGES_DOWNLOAD_ERROR
import ru.dw.astronomypictureoftheday.utils.CONSTANT_VIDEO
import ru.dw.astronomypictureoftheday.utils.convertSuccessesToEntity


class ListPhotosViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val context: Context by lazy { application.applicationContext }
    private val dataApiNasa: ListPicture = RetrofitHelper
    private val liveData: MutableLiveData<PictureAppState> = MutableLiveData()
    private val fileHelper: FileHelper = FileHelper(context)
    private val helperRoom = MyApp.getDBRoom()

    private val observer = Observer<List<DayPhotoEntity>> { listPhoto ->
        liveData.postValue(PictureAppState.Success(listPhoto))
    }

    init {
        getDataRoomListDay()
    }

    fun checkDateToRequest(date: String): Boolean {
        return if (helperRoom.getIsDate(date)) {
            viewModelScope.launch(Dispatchers.Main) {
                sendRequest(date)
            }
            true
        } else {
            false
        }
    }

    fun deleteDayPhoto(data: DayPhotoEntity) {
        helperRoom.deleteDayPhoto(data)
        fileHelper.deleteFiles(data.url)
    }

    fun getLiveData(): MutableLiveData<PictureAppState> {
        return liveData
    }

    private fun sendRequest(date: String) {
        liveData.postValue(PictureAppState.Loading)
        dataApiNasa.getListDayPicture(date, object : CallbackDetails {
            override fun onResponseSuccess(successes: List<DayPhotoResponse>) {

                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        val entity = convertSuccessesToEntity(successes[0])
                        when (entity.mediaType) {
                            CONSTANT_IMAGE -> {
                                fileHelper.downloadImages(
                                    entity.url,
                                    entity.date
                                ) { successImagesName ->

                                    if (successImagesName != CONSTANT_IMAGES_DOWNLOAD_ERROR) {
                                        entity.url = successImagesName
                                        helperRoom.setDayPhoto(entity)
                                    } else {
                                        helperRoom.setDayPhoto(entity)
                                    }
                                }
                            }
                            CONSTANT_VIDEO -> {
                                helperRoom.setDayPhoto(entity)
                            }
                        }

                    } catch (e: NullPointerException) {
                        e.message?.let {
                            liveData.postValue(PictureAppState.Error(e.message!!))
                        }
                    }
                }
            }

            override fun onFail(error: String) {
                liveData.postValue(PictureAppState.Error(error))
                Log.d("@@@", "onFail: $error")
            }

        })
    }

    override fun onCleared() {
        helperRoom.getAllListDay().removeObserver(observer)
        super.onCleared()
    }

    private fun getDataRoomListDay() {
        helperRoom.getAllListDay().observeForever(observer)
    }

}
package ru.dw.astronomypictureoftheday.ui.list.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.dw.astronomypictureoftheday.MyApp
import ru.dw.astronomypictureoftheday.data.room.DayPhotoEntity
import ru.dw.astronomypictureoftheday.model.DayPhotoResponse
import ru.dw.astronomypictureoftheday.repository.RepositoryIpl
import ru.dw.astronomypictureoftheday.utils.CONSTANT_IMAGES_DOWNLOAD_ERROR
import ru.dw.astronomypictureoftheday.data.FileHelper
import ru.dw.astronomypictureoftheday.utils.convertSuccessesToEntity


class ListPhotosViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val context:Context by lazy { application.applicationContext }

    private val repository: Repository = RepositoryIpl
    private val liveData: MutableLiveData<PictureAppState> = MutableLiveData()
    private val fileHelper: FileHelper = FileHelper(context)

    val helperRoom = MyApp.getDBRoom()

    fun deleteDayPhoto(data: DayPhotoEntity){
        helperRoom.deleteDayPhoto(data)
        fileHelper.deleteFiles(data.url)
    }

    fun getLiveData(): MutableLiveData<PictureAppState> {
        return liveData
    }

    fun sendRequest(date: String) {
        liveData.postValue(PictureAppState.Loading)
        repository.getDataList().getListDayPicture(date, object : CallbackDetails {
            override fun onResponseSuccess(successes: List<DayPhotoResponse>) {
                //Log.d("@@@", "onResponseSuccess: ${successes[0]}")
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        val entity = convertSuccessesToEntity(successes[0])

                        fileHelper.downloadImages(entity.url,entity.date){ successImagesName->

                            if (successImagesName != CONSTANT_IMAGES_DOWNLOAD_ERROR){
                                entity.url =  successImagesName
                                helperRoom.setDayPhoto(entity)
                            }else{
                                helperRoom.setDayPhoto(entity)
                            }

                        }

                    } catch (e: NullPointerException) {
                        e.message?.let {
                            liveData.postValue(PictureAppState.Error(e.message!!))
                        }
                    }
                }
                liveData.postValue(PictureAppState.Success)
            }

            override fun onFail(error: String) {
                liveData.postValue(PictureAppState.Error(error))
                Log.d("@@@", "onFail: $error")
            }

        })
    }

}
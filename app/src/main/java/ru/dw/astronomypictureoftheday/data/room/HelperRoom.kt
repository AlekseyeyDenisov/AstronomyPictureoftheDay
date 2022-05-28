package ru.dw.astronomypictureoftheday.data.room

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData

class HelperRoom(private val db:DayPhotoDao) {

     fun getAllListDay():LiveData<List<DayPhotoEntity>> {
        return db.getAll()
    }

    fun setDayPhoto(data:DayPhotoEntity){
        db.insert(data)
    }
    fun deleteDayPhoto(data:DayPhotoEntity){
        db.delete(data)
    }

    fun getIsDate(date:String):DayPhotoEntity{
       return db.getIsDate(date)
    }

}
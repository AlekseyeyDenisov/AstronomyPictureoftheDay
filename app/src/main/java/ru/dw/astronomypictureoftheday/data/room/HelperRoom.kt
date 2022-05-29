package ru.dw.astronomypictureoftheday.data.room


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

    fun getIsDate(date:String):Boolean{
        return db.getIsDate(date) == null

    }

}
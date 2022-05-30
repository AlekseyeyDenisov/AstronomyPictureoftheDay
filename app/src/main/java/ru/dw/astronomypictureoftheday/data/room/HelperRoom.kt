package ru.dw.astronomypictureoftheday.data.room


import androidx.lifecycle.LiveData

class HelperRoom(private val db: DayPhotoDao) {

    fun getAllListDay(): LiveData<List<DayPhotoEntity>> = db.getAll()

    suspend fun setDayPhoto(data: DayPhotoEntity) = db.insert(data)

    suspend fun deleteDayPhoto(data: DayPhotoEntity) = db.delete(data)

    suspend fun getIsDate(date: String): Boolean = db.getIsDate(date) == null


}
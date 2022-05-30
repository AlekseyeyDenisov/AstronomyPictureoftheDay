package ru.dw.astronomypictureoftheday.data.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DayPhotoDao {

    @Insert
    fun insert(entity: DayPhotoEntity)

    @Delete
    fun delete(entity: DayPhotoEntity)


    @Query("SELECT * FROM nasa_table ORDER BY date ASC")
    fun getAll():LiveData<List<DayPhotoEntity>>

    @Query("SELECT * FROM nasa_table WHERE date=:date ORDER BY ID DESC LIMIT 1")
    fun getIsDate(date:String): DayPhotoEntity

}
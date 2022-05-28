package ru.dw.astronomypictureoftheday.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DayPhotoEntity::class], version = 1)
abstract class DBRoom: RoomDatabase() {
    abstract fun nasaDayDao():DayPhotoDao
}
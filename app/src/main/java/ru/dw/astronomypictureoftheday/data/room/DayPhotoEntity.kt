package ru.dw.astronomypictureoftheday.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "nasa_table")
data class DayPhotoEntity(

    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val date: String,
    val hdUrl: String,
    val explanation: String,
    val title: String,
    val url: String
)
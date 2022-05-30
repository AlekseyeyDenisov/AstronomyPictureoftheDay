package ru.dw.astronomypictureoftheday.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.dw.astronomypictureoftheday.utils.EMPTY_STRING


@Entity(tableName = "nasa_table")
data class DayPhotoEntity(

    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val date: String,
    val hdUrl: String = EMPTY_STRING,
    val explanation: String = EMPTY_STRING,
    val title: String = EMPTY_STRING,
    val url: String =EMPTY_STRING,
    val mediaType:String = EMPTY_STRING
)
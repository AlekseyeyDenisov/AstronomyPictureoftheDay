package ru.dw.astronomypictureoftheday.data.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import ru.dw.astronomypictureoftheday.utils.EMPTY_STRING

@Parcelize
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
): Parcelable
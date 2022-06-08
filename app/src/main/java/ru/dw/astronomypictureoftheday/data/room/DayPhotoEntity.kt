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
    val hdUrl: String,
    val explanation: String,
    val title: String,
    var url: String,
    val mediaType:String
): Parcelable
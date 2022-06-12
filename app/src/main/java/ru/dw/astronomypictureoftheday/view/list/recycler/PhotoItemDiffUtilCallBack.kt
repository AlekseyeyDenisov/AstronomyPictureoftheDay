package ru.dw.astronomypictureoftheday.view.list.recycler

import androidx.recyclerview.widget.DiffUtil
import ru.dw.astronomypictureoftheday.data.room.DayPhotoEntity

class PhotoItemDiffUtilCallBack: DiffUtil.ItemCallback<DayPhotoEntity>() {
    override fun areItemsTheSame(
        oldPhotoResponse: DayPhotoEntity,
        newPhotoResponse: DayPhotoEntity
    ): Boolean {
        return oldPhotoResponse.date == newPhotoResponse.date
    }

    override fun areContentsTheSame(
        oldPhotoResponse: DayPhotoEntity,
        newPhotoResponse: DayPhotoEntity
    ): Boolean {
        return oldPhotoResponse == newPhotoResponse
    }
}
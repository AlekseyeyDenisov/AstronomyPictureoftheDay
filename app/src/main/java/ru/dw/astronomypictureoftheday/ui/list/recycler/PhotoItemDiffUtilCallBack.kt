package ru.dw.astronomypictureoftheday.ui.list.recycler

import androidx.recyclerview.widget.DiffUtil
import ru.dw.astronomypictureoftheday.model.DayPhotoResponse

class PhotoItemDiffUtilCallBack: DiffUtil.ItemCallback<DayPhotoResponse>() {
    override fun areItemsTheSame(
        oldPhotoResponse: DayPhotoResponse,
        newPhotoResponse: DayPhotoResponse
    ): Boolean {
        return oldPhotoResponse.date == newPhotoResponse.date
    }

    override fun areContentsTheSame(
        oldPhotoResponse: DayPhotoResponse,
        newPhotoResponse: DayPhotoResponse
    ): Boolean {
        return oldPhotoResponse == newPhotoResponse
    }
}
package ru.dw.astronomypictureoftheday.ui.list.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.dw.astronomypictureoftheday.R
import ru.dw.astronomypictureoftheday.data.room.DayPhotoEntity
import ru.dw.astronomypictureoftheday.databinding.ItemPhotoDayBinding
import ru.dw.astronomypictureoftheday.utils.CONSTANT_IMAGE

class HolderAdapterPhotoItem(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(dayPhoto: DayPhotoEntity) {
        ItemPhotoDayBinding.bind(itemView).apply {
            titleItem.text = dayPhoto.title
            dataItem.text = dayPhoto.date
            if (dayPhoto.mediaType == CONSTANT_IMAGE){
                photoItem.load(dayPhoto.url){
                    placeholder(R.drawable.loadig)
                }
            }
            else photoItem.load(R.drawable.you_tube)
        }

    }

}
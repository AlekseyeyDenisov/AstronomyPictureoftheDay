package ru.dw.astronomypictureoftheday.ui.list.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.dw.astronomypictureoftheday.data.room.DayPhotoEntity
import ru.dw.astronomypictureoftheday.databinding.ItemPhotoDayBinding

class HolderAdapterPhotoItem(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(dayPhoto: DayPhotoEntity) {
        ItemPhotoDayBinding.bind(itemView).apply {
            titleItem.text = dayPhoto.title
            dataItem.text = dayPhoto.date
            photoItem.load(dayPhoto.url)
        }

    }

}
package ru.dw.astronomypictureoftheday.ui.list.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.dw.astronomypictureoftheday.R
import ru.dw.astronomypictureoftheday.data.room.DayPhotoEntity
import ru.dw.astronomypictureoftheday.databinding.ItemPhotoDayBinding
import ru.dw.astronomypictureoftheday.utils.CONSTANT_IMAGE
import ru.dw.astronomypictureoftheday.utils.CONSTANT_VIDEO
import ru.dw.astronomypictureoftheday.utils.getUriImages

class HolderAdapterPhotoItem(
    view: View,
    private val onItemListener: OnItemListenerPhotoNasa
) :
    RecyclerView.ViewHolder(view) {
    fun bind(dayPhoto: DayPhotoEntity) {
        ItemPhotoDayBinding.bind(itemView).apply {
            titleItem.text = dayPhoto.title
            dataItem.text = dayPhoto.date
            if (dayPhoto.mediaType == CONSTANT_IMAGE && dayPhoto.url.isNotEmpty()) {
                photoItem.setImageURI(getUriImages(itemView.context,dayPhoto))

            } else photoItem.load(R.drawable.noimages)
            if (dayPhoto.mediaType == CONSTANT_VIDEO){
                photoItem.load(R.drawable.you_tube)
            }

            root.setOnClickListener {
                onItemListener.onClickListenerItem(dayPhoto)
            }
        }

    }




}
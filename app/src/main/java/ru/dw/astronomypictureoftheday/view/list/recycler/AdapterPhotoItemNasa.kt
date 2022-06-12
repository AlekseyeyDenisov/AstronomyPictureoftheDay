package ru.dw.astronomypictureoftheday.view.list.recycler


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.dw.astronomypictureoftheday.data.room.DayPhotoEntity
import ru.dw.astronomypictureoftheday.databinding.ItemPhotoDayBinding

interface OnItemListenerPhotoNasa {
    fun onClickListenerItem(dayPhotoEntity: DayPhotoEntity)
}

class AdapterPhotoItemNasa(private val onItemListener: OnItemListenerPhotoNasa):
    ListAdapter<DayPhotoEntity, HolderAdapterPhotoItem>(PhotoItemDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderAdapterPhotoItem {
        val binding =
            ItemPhotoDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderAdapterPhotoItem(binding.root, onItemListener)
    }

    override fun onBindViewHolder(holder: HolderAdapterPhotoItem, position: Int) {
        holder.bind(getItem(position))
    }

}
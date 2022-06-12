package ru.dw.astronomypictureoftheday.view.list.components

import androidx.fragment.app.FragmentActivity
import com.google.android.material.datepicker.MaterialDatePicker
import ru.dw.astronomypictureoftheday.utils.convertDateFormat


interface OnDatePicker{
    fun getResultDate(newDate:String)
}
class DayPickersDate(
    private val activity: FragmentActivity
) {
   fun materialDatePicker(onDatePicker:OnDatePicker){
        val dateRangePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select dates")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        dateRangePicker.show(activity.supportFragmentManager, "tagDataPiker")
        dateRangePicker.addOnPositiveButtonClickListener {
            onDatePicker.getResultDate(convertDateFormat(it))
        }
    }

}
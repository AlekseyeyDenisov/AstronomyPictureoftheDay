package ru.dw.astronomypictureoftheday.ui.list.components

import android.app.DatePickerDialog
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.android.material.datepicker.MaterialDatePicker
import ru.dw.astronomypictureoftheday.utils.convertDateFormat
import java.util.*


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
            Log.d("@@@", "dateRangePicker: ${convertDateFormat(dateRangePicker.headerText)}")
            val newDate = convertDateFormat(dateRangePicker.headerText)
            onDatePicker.getResultDate(newDate)
        }

    }

    fun datePickerDialog(onDatePicker:OnDatePicker){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(activity,{ _, ofYear, monthOfYear, dayOfMonth ->
            val date = "$ofYear-$monthOfYear-$dayOfMonth"
            onDatePicker.getResultDate(date)
        }, year, month, day)
        dpd.show()

    }




}
package ru.dw.astronomypictureoftheday.ui.list

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import ru.dw.astronomypictureoftheday.R
import ru.dw.astronomypictureoftheday.databinding.FragmentListPichureDayBinding
import ru.dw.astronomypictureoftheday.ui.list.recycler.AdapterPhotoItemNasa
import ru.dw.astronomypictureoftheday.ui.list.viewmodel.ListPhotosViewModel
import ru.dw.astronomypictureoftheday.utils.convertDateFormat
import ru.dw.astronomypictureoftheday.utils.getCurrentDays
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class ListOfPhotosByDayFragment : Fragment() {
    private var _binding: FragmentListPichureDayBinding? = null
    private val binding: FragmentListPichureDayBinding get() = _binding!!
    private val viewModel: ListPhotosViewModel by lazy {
        ViewModelProvider(this)[ListPhotosViewModel::class.java]
    }
    private val adapterPhoto = AdapterPhotoItemNasa()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListPichureDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initViewModel()
        initFab()
    }


    private fun initFab() {

        binding.floatingActionButton.setOnClickListener {
            val dateRangePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select dates")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())

                    .build()

            dateRangePicker.show(requireActivity().supportFragmentManager, "tagDataPiker")

            dateRangePicker.addOnPositiveButtonClickListener {
                Log.d("@@@", "dateRangePicker: ${convertDateFormat(dateRangePicker.headerText)}")
                val newDate = convertDateFormat(dateRangePicker.headerText)
                viewModel.sendRequest(newDate)
            }

        }
    }



    private fun initViewModel() {
        Log.d("@@@", "initViewModel: ${getCurrentDays()}")
        viewModel.sendRequest(getCurrentDays())
        viewModel.getLiveData().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun initRecycler() {
        binding.recyclerListPhoto.adapter = adapterPhoto
    }

    companion object {
        @JvmStatic
        fun newInstance() = ListOfPhotosByDayFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun render(data: PictureAppState) {
        when (data) {
            is PictureAppState.Error -> {
                visibilityLoading(false)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.something_went_wrong),
                    Toast.LENGTH_LONG
                ).show()
            }
            is PictureAppState.Success -> {
                visibilityLoading(false)
                adapterPhoto.submitList(data.dayPhotoResponse)
            }
            PictureAppState.Loading -> {
                visibilityLoading(true)

            }
        }
    }

    private fun visibilityLoading(visibility: Boolean) {
        if (visibility) binding.loadingItem.visibility = View.VISIBLE
        else binding.loadingItem.visibility = View.GONE
    }

}
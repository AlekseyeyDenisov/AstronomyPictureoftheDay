package ru.dw.astronomypictureoftheday.ui.list

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.dw.astronomypictureoftheday.R
import ru.dw.astronomypictureoftheday.databinding.FragmentListPichureDayBinding
import ru.dw.astronomypictureoftheday.ui.list.components.DayPickersDate
import ru.dw.astronomypictureoftheday.ui.list.components.OnDatePicker
import ru.dw.astronomypictureoftheday.ui.list.recycler.AdapterPhotoItemNasa
import ru.dw.astronomypictureoftheday.ui.list.viewmodel.ListPhotosViewModel
import ru.dw.astronomypictureoftheday.utils.getCurrentDays


class ListPhotosDayNasaFragment : Fragment() {
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
        swipedItem()
        checkDateToRequest(getCurrentDays(), true)

    }


    private fun swipedItem() {
        val callback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapterPhoto.currentList[viewHolder.adapterPosition]
                Thread {
                    viewModel.helperRoom.deleteDayPhoto(item)
                }.start()

            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerListPhoto)
    }


    private fun initFab() {
        binding.floatingActionButton.setOnClickListener {
            //DayPickersDate(requireActivity()).materialDatePicker(object : OnDatePicker {
            DayPickersDate(requireActivity()).datePickerDialog(object : OnDatePicker {
                override fun getResultDate(newDate: String) {
                    checkDateToRequest(newDate)
                }
            })
        }
    }


    private fun initViewModel() {
        viewModel.getLiveData().observe(viewLifecycleOwner) { state ->
            render(state)
        }
        viewModel.helperRoom.getAllListDay().observe(viewLifecycleOwner) { listPhoto ->
            adapterPhoto.submitList(listPhoto)
        }
    }

    private fun initRecycler() {
        binding.recyclerListPhoto.adapter = adapterPhoto
    }

    companion object {
        @JvmStatic
        fun newInstance() = ListPhotosDayNasaFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun render(data: PictureAppState) {
        when (data) {
            is PictureAppState.Error -> {
                visibilityLoading(false)
                showToast(getString(R.string.something_went_wrong))
            }
            is PictureAppState.Success -> {
                visibilityLoading(false)
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

    private fun checkDateToRequest(date: String, firstBoot: Boolean = false) {
        Thread {
            if (viewModel.helperRoom.getIsDate(date)) {
                Handler(Looper.getMainLooper()).post {
                    viewModel.sendRequest(date)
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    if (!firstBoot) showToast(date + getString(R.string.this_date_is))
                }
            }
        }.start()

    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}
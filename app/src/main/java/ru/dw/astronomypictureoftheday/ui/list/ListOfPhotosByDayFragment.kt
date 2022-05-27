package ru.dw.astronomypictureoftheday.ui.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import ru.dw.astronomypictureoftheday.R
import ru.dw.astronomypictureoftheday.databinding.FragmentListPichureDayBinding
import ru.dw.astronomypictureoftheday.ui.list.recycler.AdapterPhotoItemNasa
import ru.dw.astronomypictureoftheday.ui.list.viewmodel.ListPhotosViewModel
import ru.dw.astronomypictureoftheday.utils.getDaysAgo


class ListOfPhotosByDayFragment : Fragment() {
    private var _binding: FragmentListPichureDayBinding? = null
    private val binding:FragmentListPichureDayBinding get() = _binding!!
    private val viewModel: ListPhotosViewModel by lazy {
        ViewModelProvider(this)[ListPhotosViewModel::class.java]
    }
    private val adapterPhoto = AdapterPhotoItemNasa()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListPichureDayBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(requireContext(), "ok", Toast.LENGTH_SHORT).show()

        initRecycler()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.sendRequest(getDaysAgo(0))
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
                Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
            }
            is PictureAppState.Success -> {
                Log.d("@@@", "render Success: "+ data.dayPhotoResponse[0].title)
                adapterPhoto.submitList(data.dayPhotoResponse)
            }
            PictureAppState.Loading -> {
                Log.d("@@@", "render: Loading")
            }
        }
    }

}
package ru.dw.astronomypictureoftheday.ui.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import ru.dw.astronomypictureoftheday.databinding.FragmentListPichureDayBinding
import ru.dw.astronomypictureoftheday.ui.list.viewmodel.ListPhotosViewModel
import ru.dw.astronomypictureoftheday.util.getDaysAgo


class ListOfPhotosByDayFragment : Fragment() {
    private var _binding: FragmentListPichureDayBinding? = null
    private val binding:FragmentListPichureDayBinding get() = _binding!!
    private val viewModel: ListPhotosViewModel by lazy {
        ViewModelProvider(this)[ListPhotosViewModel::class.java]
    }


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
        viewModel.sendRequest(getDaysAgo(0))
    }

    companion object {
        @JvmStatic
        fun newInstance() = ListOfPhotosByDayFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
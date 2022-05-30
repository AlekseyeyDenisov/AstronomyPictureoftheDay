package ru.dw.astronomypictureoftheday.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import ru.dw.astronomypictureoftheday.R
import ru.dw.astronomypictureoftheday.data.room.DayPhotoEntity
import ru.dw.astronomypictureoftheday.databinding.FragmentDetailsBinding
import ru.dw.astronomypictureoftheday.utils.CONSTANT_VIDEO

const val KEY_BUNDLE_DETAILS = "KEY_BUNDLE_DETAILS"


class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var dayPhotoEntity: DayPhotoEntity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dayPhotoEntity = it.getParcelable<DayPhotoEntity>(KEY_BUNDLE_DETAILS) as DayPhotoEntity
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
       // Log.d("@@@", "onViewCreated: $dayPhotoEntity")
        binding.bottomSheetLayout.titleBottomSheet.text = dayPhotoEntity.title
        binding.bottomSheetLayout.explanationBottomSheet.text = dayPhotoEntity.explanation

        if (dayPhotoEntity.mediaType == CONSTANT_VIDEO){
            binding.detailsImageLayout.apply {
                load(R.drawable.you_tube)
                setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(dayPhotoEntity.url)
                    })                }
            }
        }else {
            binding.detailsImageLayout.load(dayPhotoEntity.hdUrl) {
                placeholder(R.drawable.loadig)
            }
        }

    }
    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle) =
            DetailsFragment().apply {
                arguments = bundle
            }
    }
}
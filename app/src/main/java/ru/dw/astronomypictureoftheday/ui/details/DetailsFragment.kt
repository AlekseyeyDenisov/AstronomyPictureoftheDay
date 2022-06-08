package ru.dw.astronomypictureoftheday.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import ru.dw.astronomypictureoftheday.MyApp
import ru.dw.astronomypictureoftheday.R
import ru.dw.astronomypictureoftheday.data.room.DayPhotoEntity
import ru.dw.astronomypictureoftheday.databinding.FragmentDetailsBinding
import ru.dw.astronomypictureoftheday.utils.CONSTANT_VIDEO
import ru.dw.astronomypictureoftheday.utils.getUriImages


const val KEY_BUNDLE_DETAILS = "KEY_BUNDLE_DETAILS"


class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var dayPhotoEntity: DayPhotoEntity
    private lateinit var youTubePlayerView: YouTubePlayerView
    private var isConnect:Boolean = false


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
        MyApp.isConnectivity.observe(viewLifecycleOwner){
            isConnect = it
            isInternetConnect()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isInternetConnect()
        initYouTube()
        initView()
    }

    private fun initYouTube() {
        youTubePlayerView = binding.youtubePlayerView
        lifecycle.addObserver(youTubePlayerView)

    }

    private fun initView() {
        binding.bottomSheetLayout.titleBottomSheet.text = dayPhotoEntity.title
        binding.bottomSheetLayout.explanationBottomSheet.text = dayPhotoEntity.explanation

        if (dayPhotoEntity.mediaType == CONSTANT_VIDEO) {
            if (isConnect) {
                isVisibleVideo(true)
                showNasaVideo(parseUrl(dayPhotoEntity.url))
            } else {
                binding.detailsImageLayout.load(R.drawable.you_tube)
                binding.detailsImageLayout.setOnClickListener {
                    if (isConnect) {
                        isVisibleVideo(true)
                        showNasaVideo(parseUrl(dayPhotoEntity.url))
                    }
                }
            }

        } else {
            isVisibleVideo(false)
            binding.detailsImageLayout.setImageURI(getUriImages(requireContext(), dayPhotoEntity))

        }

    }

    private fun isInternetConnect() {
         if (isConnect) binding.infoError.infoErrorOnline.visibility = View.GONE
         else  binding.infoError.infoErrorOnline.visibility = View.VISIBLE
    }

    private fun showNasaVideo(videoId: String) {
        youTubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0F)
            }
        })

    }

    private fun isVisibleVideo(visibleVideo: Boolean) = if (visibleVideo) {
        binding.detailsImageLayout.visibility = View.GONE
        binding.youtubePlayerView.visibility = View.VISIBLE
    } else {
        binding.detailsImageLayout.visibility = View.VISIBLE
        binding.youtubePlayerView.visibility = View.GONE
    }

    private fun parseUrl(idVideo: String): String = idVideo.removeSurrounding(
        "https://www.youtube.com/embed/", "?rel=0"
    )

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle) =
            DetailsFragment().apply {
                arguments = bundle
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        youTubePlayerView.release()
    }
}
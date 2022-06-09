package ru.dw.astronomypictureoftheday.ui.details

import android.os.Bundle
import android.util.Log
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
import ru.dw.astronomypictureoftheday.utils.*


const val KEY_BUNDLE_DETAILS = "KEY_BUNDLE_DETAILS"


class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var dayPhotoEntity: DayPhotoEntity
    private lateinit var youTubePlayerView: YouTubePlayerView
    // private var isConnect: Boolean = false


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
        initYouTube()
        initView(false)
        MyApp.isConnectivity.observe(viewLifecycleOwner) { isConnect ->
            initView(isConnect)
        }

    }

    private fun initYouTube() {
        youTubePlayerView = binding.youtubePlayerView
        lifecycle.addObserver(youTubePlayerView)

    }

    private fun initView(isConnectivity: Boolean) {
        visibilityErrorInfo(isConnectivity)
        binding.bottomSheetLayout.titleBottomSheet.text = dayPhotoEntity.title
        binding.bottomSheetLayout.explanationBottomSheet.text = dayPhotoEntity.explanation
        when (dayPhotoEntity.mediaType) {

            CONSTANT_IMAGE -> howNasaImages()

            CONSTANT_VIDEO -> loadVideo(isConnectivity)

        }
    }


    private fun loadVideo(isConnectivity: Boolean) {
        when (isConnectivity) {

            CONSTANT_ONLINE -> showNasaVideo(parseUrl(dayPhotoEntity.url), true)

            CONSTANT_OFFLINE -> showImagesYoutube()
            else -> {
                Log.d("@@@", "loadVideo else: ")
            }
        }
    }

    private fun howNasaImages() {
        isVisibleVideo(false)
        binding.detailsImageLayout.setImageURI(getUriImages(requireContext(), dayPhotoEntity))
    }

    private fun showNasaVideo(videoId: String, play: Boolean) {
        Log.d("@@@", "showNasaVideo: $videoId")
        isVisibleVideo(true)
        youTubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                if (play) youTubePlayer.loadVideo(videoId, 0F)
                else youTubePlayer.pause()

            }
        })
    }


    private fun showImagesYoutube() {
        showNasaVideo(parseUrl(dayPhotoEntity.url), false)
        isVisibleVideo(false)
        binding.detailsImageLayout.load(R.drawable.you_tube)
    }


    private fun isVisibleVideo(visibleVideo: Boolean) {
        if (visibleVideo) {
            binding.detailsImageLayout.visibility = View.GONE
            binding.youtubePlayerView.visibility = View.VISIBLE
        } else {
            binding.detailsImageLayout.visibility = View.VISIBLE
            binding.youtubePlayerView.visibility = View.GONE
        }
    }

    private fun parseUrl(idVideo: String): String = idVideo.removeSurrounding(
        "https://www.youtube.com/embed/", "?rel=0"
    )


    private fun visibilityErrorInfo(isConnect: Boolean) {
        if (isConnect) binding.infoError?.infoErrorOnline?.visibility = View.GONE
        else binding.infoError?.infoErrorOnline?.visibility = View.VISIBLE
    }

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
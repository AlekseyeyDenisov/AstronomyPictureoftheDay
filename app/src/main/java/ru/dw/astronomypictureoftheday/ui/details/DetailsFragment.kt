package ru.dw.astronomypictureoftheday.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import ru.dw.astronomypictureoftheday.R
import ru.dw.astronomypictureoftheday.data.room.DayPhotoEntity
import ru.dw.astronomypictureoftheday.databinding.FragmentDetailsBinding
import ru.dw.astronomypictureoftheday.utils.CONSTANT_VIDEO

const val KEY_BUNDLE_DETAILS = "KEY_BUNDLE_DETAILS"


class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var dayPhotoEntity: DayPhotoEntity
    lateinit var  youTubePlayerView: YouTubePlayerView


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
        initView()
    }
    private fun initYouTube() {
        youTubePlayerView = binding.youtubePlayerView
        lifecycle.addObserver(youTubePlayerView)
    }

    private fun initView() {
        binding.bottomSheetLayout.titleBottomSheet.text = dayPhotoEntity.title
        binding.bottomSheetLayout.explanationBottomSheet.text = dayPhotoEntity.explanation

        if (dayPhotoEntity.mediaType == CONSTANT_VIDEO){
            isVisibleVideo(true)
            showNasaVideo(parseUrl(dayPhotoEntity.url),true)

        }else {
            isVisibleVideo(false)
            binding.detailsImageLayout.load(dayPhotoEntity.hdUrl) {
                placeholder(R.drawable.loadig)
            }
        }

    }
    private fun showNasaVideo(videoId:String,isPlay:Boolean){
        youTubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                if (isPlay)youTubePlayer.loadVideo(videoId,0F)
                else youTubePlayer.pause()
            }
        })
    }
    private fun isVisibleVideo(visible:Boolean) {
        if (visible){
            binding.detailsImageLayout.visibility = View.GONE
            binding.youtubePlayerView.visibility = View.VISIBLE
        }else{
            binding.detailsImageLayout.visibility = View.VISIBLE
            binding.youtubePlayerView.visibility = View.GONE
        }

    }
    private fun parseUrl(idVideo: String): String =  idVideo.removeSurrounding(
        "https://www.youtube.com/embed/","?rel=0"
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
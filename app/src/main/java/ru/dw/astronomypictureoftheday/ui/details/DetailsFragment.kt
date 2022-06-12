package ru.dw.astronomypictureoftheday.ui.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
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

    private var isFullScreen: Boolean = false
    private var isOpenBottomSheet: Boolean = false


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

        MyApp.isConnectivity.observe(viewLifecycleOwner) { isConnect ->
            showIsConnectivity(isConnect)
        }

    }

    private fun initYouTube() {
        youTubePlayerView = binding.youtubePlayerView
        lifecycle.addObserver(youTubePlayerView)

    }

    private fun showIsConnectivity(isConnectivity: Boolean) {
        visibilityErrorInfo(isConnectivity)
        when (dayPhotoEntity.mediaType) {
            CONSTANT_IMAGE -> nasaImages()
            CONSTANT_VIDEO -> loadVideo(isConnectivity)
        }
    }

    private fun initView() {
        binding.title?.text = dayPhotoEntity.title
        binding.date?.text = dayPhotoEntity.date
        binding.bottomSheetLayout.explanationBottomSheet.text = dayPhotoEntity.explanation
        animateDescription()
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

    private fun nasaImages() {
        isVisibleVideo(false)
        binding.detailsImageLayout.setImageURI(getUriImages(requireContext(), dayPhotoEntity))
        animateZoom()


    }

    private fun animateDescription() {


        val standardBottomSheetBehavior =
            BottomSheetBehavior.from(binding.bottomSheetLayout.bottomSheet)
        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

                if (slideOffset == 0F && isOpenBottomSheet) {
                    showTitleHide()


                } else if (slideOffset == 1F && !isOpenBottomSheet) {
                    showTitle()

                }
            }
        }
        standardBottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)


    }

    private fun showTitle() {
        transition()
        val constrainSet = constraintSet()
        constrainSet.clear(R.id.title, ConstraintSet.END)
        constrainSet.connect(
            R.id.title,
            ConstraintSet.START,
            R.id.constraint_container,
            ConstraintSet.START
        )
        applyConstrain(constrainSet)
    }

    private fun showTitleHide() {
        transition()
        val constrainSet = constraintSet()
        constrainSet.clear(R.id.title, ConstraintSet.START)
        constrainSet.connect(
            R.id.title,
            ConstraintSet.END,
            R.id.constraint_container,
            ConstraintSet.START
        )
        applyConstrain(constrainSet)

    }

    private fun applyConstrain(constrainSet: ConstraintSet) {
        constrainSet.applyTo(binding.constraintContainer)
    }

    private fun constraintSet(): ConstraintSet {
        val constrainSet = ConstraintSet()
        constrainSet.clone(binding.constraintContainer)
        return constrainSet
    }

    private fun transition() {
        isOpenBottomSheet = !isOpenBottomSheet
        val transition = ChangeBounds()
        transition.interpolator = AnticipateInterpolator()
        transition.duration = 500
        TransitionManager.beginDelayedTransition(binding.constraintContainer, transition)
    }

    private fun animateZoom() {
        binding.detailsImageLayout.setOnClickListener {
            isFullScreen = !isFullScreen

            val transitionImageTransform = ChangeImageTransform()
            transitionImageTransform.duration = 500

            val transitionSet = TransitionSet()
            transitionSet.addTransition(transitionImageTransform)
            TransitionManager.beginDelayedTransition(binding.root, transitionSet)

            binding.detailsImageLayout.scaleType = if (isFullScreen) {
                ImageView.ScaleType.CENTER_CROP
            } else {
                ImageView.ScaleType.CENTER_INSIDE
            }
        }
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
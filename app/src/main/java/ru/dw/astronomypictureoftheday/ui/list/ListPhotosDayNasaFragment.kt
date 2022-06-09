package ru.dw.astronomypictureoftheday.ui.list


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.dw.astronomypictureoftheday.MyApp
import ru.dw.astronomypictureoftheday.R
import ru.dw.astronomypictureoftheday.data.room.DayPhotoEntity
import ru.dw.astronomypictureoftheday.databinding.FragmentListPictureDayBinding
import ru.dw.astronomypictureoftheday.ui.details.DetailsFragment
import ru.dw.astronomypictureoftheday.ui.details.KEY_BUNDLE_DETAILS
import ru.dw.astronomypictureoftheday.ui.list.components.DayPickersDate
import ru.dw.astronomypictureoftheday.ui.list.components.OnDatePicker
import ru.dw.astronomypictureoftheday.ui.list.recycler.AdapterPhotoItemNasa
import ru.dw.astronomypictureoftheday.ui.list.recycler.OnItemListenerPhotoNasa
import ru.dw.astronomypictureoftheday.ui.list.viewmodel.ListPhotosViewModel
import ru.dw.astronomypictureoftheday.ui.list.viewmodel.PictureAppState
import ru.dw.astronomypictureoftheday.utils.getCurrentDays


class ListPhotosDayNasaFragment : Fragment(), OnItemListenerPhotoNasa {
    private var _binding: FragmentListPictureDayBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListPhotosViewModel by lazy {
        ViewModelProvider(this)[ListPhotosViewModel::class.java]
    }
    private val adapterPhoto = AdapterPhotoItemNasa(this)
    private var hashListPhoto: MutableList<DayPhotoEntity> = mutableListOf()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListPictureDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initRecycler()
        swipedItem()
        checkDateToRequest(getCurrentDays(), true)
        initFab()

    }

    private fun isInternetConnect(isConnect: (Boolean) ->Unit) {
        MyApp.isConnectivity.observe(viewLifecycleOwner){
            if (it){
                binding.infoError.infoErrorOnline.visibility = View.GONE
                isConnect(true)
            } else{
                binding.infoError.infoErrorOnline.visibility = View.VISIBLE
                isConnect(false)
            }
        }

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
                val item = adapterPhoto.currentList[viewHolder.bindingAdapterPosition]
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.deleteDayPhoto(item)
                }

            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerListPhoto)
    }

    override fun onClickListenerItem(dayPhotoEntity: DayPhotoEntity) {
        val bundle = Bundle()
        bundle.putParcelable(KEY_BUNDLE_DETAILS, dayPhotoEntity)
        DetailsFragment.newInstance(bundle)
        if (isOnePanelMode()) {
            launchFragment(
                DetailsFragment.newInstance(bundle),
                R.id.container
            )
        } else {
            requireActivity().supportFragmentManager.popBackStack()
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.details_item_container, DetailsFragment.newInstance(bundle))
                .setCustomAnimations(R.anim.to_left_in, R.anim.to_left_out, R.anim.to_right_in, R.anim.to_right_out)
                .addToBackStack(null)
                .commit()

        }
    }

    private fun render(data: PictureAppState) {
        when (data) {
            is PictureAppState.Error -> {
                visibilityLoading(false)
                showToast(getString(R.string.something_went_wrong))
            }
            is PictureAppState.Success -> {
                visibilityLoading(false)
                hashListPhoto = data.listPhoto.toMutableList()
                adapterPhoto.submitList(hashListPhoto)

            }
            PictureAppState.Loading -> {
                visibilityLoading(true)

            }
        }
    }

    private fun initFab() {
        binding.floatingActionButton.setOnClickListener {

           isInternetConnect() {
               if (it)
                DayPickersDate(requireActivity()).materialDatePicker(object : OnDatePicker {

                    override fun getResultDate(newDate: String) {
                        checkDateToRequest(newDate)
                    }
                })
               else showToast(getString(R.string.no_internet_connection))
            }
        }

    }

    private fun initViewModel() {
        viewModel.getLiveData().observe(viewLifecycleOwner) { state ->
            render(state)
        }

    }

    private fun initRecycler() {
        binding.recyclerListPhoto.adapter = adapterPhoto
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    private fun launchFragment(fragment: Fragment, containerId: Int) {
        requireActivity().supportFragmentManager.popBackStack()
        requireActivity().supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.to_left_in, R.anim.to_left_out, R.anim.to_right_in, R.anim.to_right_out)
            .add(containerId, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun checkDateToRequest(date: String, firstBoot: Boolean = false) {
       isInternetConnect() {
           if (it){
               lifecycleScope.launch(Dispatchers.IO) {
                   if (!viewModel.checkDateToRequest(date)) {
                       launch(Dispatchers.Main) {
                           if (!firstBoot) showToast(date + " " + getString(R.string.this_date_is))
                       }
                   }
               }
           }
        }
    }

    private fun visibilityLoading(visibility: Boolean) {
        if (visibility) binding.loadingItem.visibility = View.VISIBLE
        else binding.loadingItem.visibility = View.GONE
    }

    private fun isOnePanelMode(): Boolean {
        return binding.detailsItemContainer == null
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = ListPhotosDayNasaFragment()
    }


}
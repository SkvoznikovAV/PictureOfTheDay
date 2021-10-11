package geekbarains.material.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProviders
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import geekbarains.material.R
import geekbarains.material.databinding.FragmentPrevPictureOfTheDayBinding
import geekbarains.material.ui.entities.PictureOfTheDayData
import geekbarains.material.ui.viewModels.PrevPictureOfTheDayViewModel
import kotlinx.android.synthetic.main.fragment_prev_picture_of_the_day.*
import kotlinx.android.synthetic.main.fragment_prev_picture_of_the_day.image_view
import kotlinx.android.synthetic.main.picture_of_the_day_description.*

class PrevPictureOfTheDayFragment : Fragment() {
    private lateinit var pictureDescriptionBottomSheetBehavior: BottomSheetBehavior<View>
    private val viewModel: PrevPictureOfTheDayViewModel by lazy {
        ViewModelProviders.of(this).get(PrevPictureOfTheDayViewModel::class.java)
    }

    private var isExpandedPicture = false

    private lateinit var repDate : String
    private var _binding: FragmentPrevPictureOfTheDayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrevPictureOfTheDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setPictureDescriptionBottomSheet(view : View) {
        pictureDescriptionBottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.pictureOfTheDayDescriptionContainer))
        pictureDescriptionBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        childFragmentManager.beginTransaction()
            .replace(R.id.pictureOfTheDayDescriptionContainer, PictureDescriptionFragment())
            .commitNow()
    }

    private fun loadAndRenderData(){
        viewModel.getData(repDate).observe(this@PrevPictureOfTheDayFragment, { renderData(it) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repDate = arguments?.getString(DATE).toString()

        setPictureDescriptionBottomSheet(view)
        loadAndRenderData()
    }

    private fun setPictureClickListener(){
        with (binding) {
            image_view.setOnClickListener {
                isExpandedPicture = !isExpandedPicture

                TransitionManager.beginDelayedTransition(
                    imageViewContainer, TransitionSet()
                        .addTransition(ChangeBounds())
                        .addTransition(ChangeImageTransform())
                )

                val params: ViewGroup.LayoutParams = imageView.layoutParams
                params.height = if (isExpandedPicture) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
                imageView.layoutParams = params
                imageView.scaleType = if (isExpandedPicture) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
            }
        }
    }

    private fun renderData(data: PictureOfTheDayData) = with (binding) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                pictureOfTheDayLoadingLayout.visibility = View.GONE
                pictureOfTheDayErrorLayout.visibility = View.GONE
                pictureOfTheDayLayout.visibility = View.VISIBLE
                pictureOfTheDayDescriptionContainer.visibility = View.VISIBLE

                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {

                } else {
                    image_view.load(url) {
                        lifecycle(this@PrevPictureOfTheDayFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    bottom_sheet_description_header.text = serverResponseData.title
                    bottom_sheet_description.text = serverResponseData.explanation

                    setPictureClickListener()
                }
            }
            is PictureOfTheDayData.Loading -> {
                pictureOfTheDayLayout.visibility = View.GONE
                pictureOfTheDayDescriptionContainer.visibility = View.GONE
                pictureOfTheDayErrorLayout.visibility = View.GONE
                pictureOfTheDayLoadingLayout.visibility = View.VISIBLE
            }
            is PictureOfTheDayData.Error -> {
                pictureOfTheDayLayout.visibility = View.GONE
                pictureOfTheDayDescriptionContainer.visibility = View.GONE
                pictureOfTheDayLoadingLayout.visibility = View.GONE
                pictureOfTheDayErrorLayout.visibility = View.VISIBLE

                txtError.text=String.format("%s %s",getString(R.string.str_prefix_error),data.error.message)
            }
        }
    }

    companion object {
        private const val DATE = "DATE"

        fun newInstance(date : String) = PrevPictureOfTheDayFragment().apply {
            arguments = Bundle(1).apply {
                putString(DATE,date)
            }
        }
    }
}
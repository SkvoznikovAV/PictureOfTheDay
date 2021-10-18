package geekbarains.material.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
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
import geekbarains.material.util.EquilateralImageView
import kotlinx.android.synthetic.main.fragment_prev_picture_of_the_day.*
import kotlinx.android.synthetic.main.fragment_prev_picture_of_the_day.image_view
import kotlinx.android.synthetic.main.picture_of_the_day_description.*

class PrevPictureOfTheDayFragment : PictureOfTheDayBaseFragment() {
    private lateinit var pictureDescriptionBottomSheetBehavior: BottomSheetBehavior<View>
    private val viewModel: PrevPictureOfTheDayViewModel by lazy {
        ViewModelProviders.of(this).get(PrevPictureOfTheDayViewModel::class.java)
    }

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
        viewModel.getData(repDate).observe(this@PrevPictureOfTheDayFragment, { renderDataLoc(it) })
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

    private fun renderDataLoc(data: PictureOfTheDayData) = with (binding) {
        renderData(data,
            pictureOfTheDayLoadingLayout,
            pictureOfTheDayErrorLayout,
            pictureOfTheDayLayout,
            pictureOfTheDayDescriptionContainer,
            imageView,
            imageViewContainer,
            this@PrevPictureOfTheDayFragment,
            txtError
        )
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
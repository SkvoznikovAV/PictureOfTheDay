package geekbarains.material.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import geekbarains.material.R
import geekbarains.material.ui.activities.MainActivity
import geekbarains.material.ui.entities.PictureOfTheDayData
import geekbarains.material.ui.viewModels.PictureOfTheDayViewModel
import kotlinx.android.synthetic.main.fragment_picture_of_the_day.*
import kotlinx.android.synthetic.main.picture_of_the_day_description.*
import geekbarains.material.databinding.FragmentPictureOfTheDayBinding
import geekbarains.material.util.EquilateralImageView
import kotlinx.android.synthetic.main.fragment_prev_picture_of_the_day.*

class PictureOfTheDayFragment : PictureOfTheDayBaseFragment() {
    private var _binding: FragmentPictureOfTheDayBinding? = null
    private val binding get() = _binding!!

    private lateinit var pictureDescriptionBottomSheetBehavior: BottomSheetBehavior<View>
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProviders.of(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPictureOfTheDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setPictureDescriptionBottomSheet(view : View) {
        childFragmentManager.beginTransaction()
            .replace(R.id.pictureOfTheDayDescriptionContainer, PictureDescriptionFragment())
            .commitNow()

        pictureDescriptionBottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.pictureOfTheDayDescriptionContainer))
        pictureDescriptionBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun loadAndRenderData(){
        viewModel.getData().observe(this@PictureOfTheDayFragment, { renderDataLoc(it) })
    }

    private fun refreshData(){
        viewModel.getData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPictureDescriptionBottomSheet(view)
        setBottomAppBar(view)
        setInputWiki()

        loadAndRenderData()
    }

    private fun setInputWiki() {
        with (binding){
            inputWiki.setEndIconOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://en.wikipedia.org/wiki/${input_wiki_edit_text.text.toString()}")
                })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_like -> {
                pictureMotionLayout.setTransition(R.id.starting_like,R.id.ending_like)
                pictureMotionLayout.transitionToEnd()
            }
            R.id.app_bar_settings -> {
                activity?.
                supportFragmentManager?.
                beginTransaction()?.
                replace(R.id.container, SettingsFragment())?.
                addToBackStack(null)?.
                commit()
            }
            android.R.id.home -> {
                activity?.let {
                    PictureBottomMenuFragment().show(it.supportFragmentManager, null)
                }
            }
            R.id.app_bar_refresh -> {
                refreshData()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun renderDataLoc(data: PictureOfTheDayData) = with (binding) {
        renderData(data,
            pictureOfTheDayLoadingLayout,
            pictureOfTheDayErrorLayout,
            pictureOfTheDayLayout,
            pictureOfTheDayDescriptionContainer,
            imageView,
            pictureMotionLayout,
            this@PictureOfTheDayFragment,
            txtError
        )

        when (data) {
            is PictureOfTheDayData.Success -> {
                wikiButton.setOnClickListener {
                    pictureMotionLayout.setTransition(R.id.starting_wiki, R.id.ending_wiki)
                    pictureMotionLayout.transitionToEnd()
                }
            }
        }
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.picture_of_the_day_bottom_app_bar))
        setHasOptionsMenu(true)
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
    }
}

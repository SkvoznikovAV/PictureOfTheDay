package geekbarains.material.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import geekbarains.material.R
import geekbarains.material.databinding.PictureOfTheDayFragmentBinding
import geekbarains.material.ui.activities.MainActivity
import geekbarains.material.ui.entities.PictureOfTheDayData
import geekbarains.material.ui.viewModels.PictureOfTheDayViewModel
import kotlinx.android.synthetic.main.picture_of_the_day_description.*
import kotlinx.android.synthetic.main.picture_of_the_day_fragment.*

class PictureOfTheDayFragment : Fragment() {
    private var _binding: PictureOfTheDayFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var pictureDescriptionBottomSheetBehavior: BottomSheetBehavior<View>
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProviders.of(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PictureOfTheDayFragmentBinding.inflate(inflater, container, false)
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
        viewModel.getData().observe(this@PictureOfTheDayFragment, Observer<PictureOfTheDayData> { renderData(it) })
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
            R.id.app_bar_fav -> toast(getString(R.string.msg_like))
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
                    PictureBottomMenuFragment().show(it.supportFragmentManager, "tag")
                }
            }
            R.id.app_bar_refresh -> {
                loadAndRenderData()
            }
        }
        return super.onOptionsItemSelected(item)
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
                    toast(getString(R.string.msg_linkisempty))
                } else {
                    image_view.load(url) {
                        lifecycle(this@PictureOfTheDayFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    bottom_sheet_description_header.text = serverResponseData.title
                    bottom_sheet_description.text = serverResponseData.explanation
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

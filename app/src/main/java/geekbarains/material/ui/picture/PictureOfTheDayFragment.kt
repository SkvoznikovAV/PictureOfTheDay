package geekbarains.material.ui.picture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import geekbarains.material.R
import geekbarains.material.databinding.MainFragmentBinding
import geekbarains.material.ui.MainActivity
import geekbarains.material.ui.SettingsFragment
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.android.synthetic.main.main_fragment.*

class PictureOfTheDayFragment : Fragment() {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProviders.of(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetBehavior(view.findViewById(R.id.bottomSheetContainer))

        childFragmentManager.beginTransaction()
            .replace(R.id.bottomSheetContainer, BottomSheetFragment())
            .commitNow()

        input_layout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${input_edit_text.text.toString()}")
            })
        }
        viewModel.getData()
            .observe(this@PictureOfTheDayFragment, Observer<PictureOfTheDayData> { renderData(it) })
        setBottomAppBar(view)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> toast(getString(R.string.msg_like))
            R.id.app_bar_settings -> activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, SettingsFragment())?.addToBackStack(null)?.commit()
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun renderData(data: PictureOfTheDayData) = with (binding) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                mainFragmentLoadingLayout.visibility = View.GONE
                mainFragmentErrorLayout.visibility = View.GONE
                mainFragmentPictureLayout.visibility = View.VISIBLE
                bottomSheetContainer.visibility = View.VISIBLE

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
                mainFragmentPictureLayout.visibility = View.GONE
                bottomSheetContainer.visibility = View.GONE
                mainFragmentErrorLayout.visibility = View.GONE
                mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is PictureOfTheDayData.Error -> {
                mainFragmentPictureLayout.visibility = View.GONE
                bottomSheetContainer.visibility = View.GONE
                mainFragmentLoadingLayout.visibility = View.GONE
                mainFragmentErrorLayout.visibility = View.VISIBLE

                txtError.text=String.format("%s %s",getString(R.string.str_prefix_error),data.error.message)
            }
        }
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)
        fab.setOnClickListener {
            if (isMain) {
                isMain = false
                bottom_app_bar.navigationIcon = null
                bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_back_fab))
                bottom_app_bar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                bottom_app_bar.navigationIcon =
                    ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
                bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_plus_fab))
                bottom_app_bar.replaceMenu(R.menu.menu_bottom_bar)
            }
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: View) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
        private var isMain = true
    }
}

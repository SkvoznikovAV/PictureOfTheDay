package geekbarains.material.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import geekbarains.material.R
import geekbarains.material.ui.activities.PreviousPictureOfTheDayActivity
import kotlinx.android.synthetic.main.bottom_navigation_layout.*

class PictureBottomMenuFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_navigation_layout, container, false)
    }

    private fun openAboutFragment(){
        activity?.
        supportFragmentManager?.
        beginTransaction()?.
        replace(R.id.container, AboutFragment())?.
        addToBackStack(null)?.
        commit()
    }

    private fun openPreviousPictureOfTheDayActivity(){
        activity?.let {
            val intent = Intent(it, PreviousPictureOfTheDayActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        navigation_view.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.previousPictureOfTheDay -> openPreviousPictureOfTheDayActivity()
                R.id.about -> openAboutFragment()
            }
            this.dismiss()
            true
        }
    }
}

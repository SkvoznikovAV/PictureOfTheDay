package geekbarains.material.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import geekbarains.material.R
import geekbarains.material.notes.activities.NotesActivity
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
            startActivity(Intent(it, PreviousPictureOfTheDayActivity::class.java))
        }
    }

    private fun openNotesActivity() {
        activity?.let {
            startActivity(Intent(it, NotesActivity::class.java))
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        navigation_view.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.previousPictureOfTheDay -> openPreviousPictureOfTheDayActivity()
                R.id.about -> openAboutFragment()
                R.id.notes -> openNotesActivity()
            }
            this.dismiss()
            true
        }
    }
}

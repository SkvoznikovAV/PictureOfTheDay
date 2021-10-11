package geekbarains.material.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import geekbarains.material.ui.fragments.PrevPictureOfTheDayFragment

class PrevPictureOfTheDayViewPagerAdapter(private val fragmentManager: FragmentManager,
                                          private val arrPrevDates: Array<String> ): FragmentStatePagerAdapter(fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return PrevPictureOfTheDayFragment.newInstance(arrPrevDates[position])
    }

    override fun getCount(): Int {
        return arrPrevDates.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return null
    }


}
package geekbarains.material.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatTextView
import geekbarains.material.R
import geekbarains.material.ui.adapters.PrevPictureOfTheDayViewPagerAdapter
import kotlinx.android.synthetic.main.activity_previous_picture_of_the_day.*
import kotlinx.android.synthetic.main.prev_date_tab_layout.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Активити для вывода фотографий за предыдущие дни
 *
 */

class PreviousPictureOfTheDayActivity : BaseActivity() {
    private lateinit var arrPrevDates : Array<String>

    /**
     * Инициализация массива предыдущих дат
     */
    @SuppressLint("SimpleDateFormat")
    private fun initDates() {
        val sdf = SimpleDateFormat("dd.M.yyyy")

        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE,-1)
        val yesterdayDate = sdf.format(cal.time)

        cal.add(Calendar.DATE,-1)
        val beforeYesterdayDate = sdf.format(cal.time)

        cal.add(Calendar.DATE,-1)
        val before2YesterdayDate = sdf.format(cal.time)

        arrPrevDates = arrayOf(yesterdayDate,beforeYesterdayDate,before2YesterdayDate)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous_picture_of_the_day)

        setTheme()
        initDates()

        prevDateViewPager.adapter = PrevPictureOfTheDayViewPagerAdapter(supportFragmentManager,
            arrPrevDates)
        prevDateTabLayout.setupWithViewPager(prevDateViewPager)
        setAllTabs()
    }


    /**
     * Функция заполнения заголовков закладок
     */
    @SuppressLint("InflateParams")
    private fun setAllTabs() {
        val layoutInflater = LayoutInflater.from(this@PreviousPictureOfTheDayActivity)

        for (i in 0 until arrPrevDates.size){
            val prevDateTabView = layoutInflater.inflate(R.layout.prev_date_tab_layout, null)
            prevDateTabView.findViewById<AppCompatTextView>(R.id.txt_prev_date_tab).apply{
                setText(arrPrevDates[i])
            }
            prevDateTabLayout.getTabAt(i)?.customView = prevDateTabView
        }
    }
}
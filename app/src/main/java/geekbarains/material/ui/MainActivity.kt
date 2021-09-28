package geekbarains.material.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import geekbarains.material.R
import kotlinx.android.synthetic.main.fragment_settings.*

class MainActivity : AppCompatActivity() {
    private val EARTH_THEME = "EARTH_THEME"
    private val MOON_THEME = "MOON_THEME"
    private val MARS_THEME = "MARS_THEME"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (getPreferences(Context.MODE_PRIVATE).getBoolean(EARTH_THEME, false)) {
            setTheme(R.style.AppEarthTheme)
        }
        if (getPreferences(Context.MODE_PRIVATE).getBoolean(MOON_THEME, false)) {
            setTheme(R.style.AppMoonTheme)
        }
        if (getPreferences(Context.MODE_PRIVATE).getBoolean(MARS_THEME, false)) {
            setTheme(R.style.AppMarsTheme)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commitNow()
        }
    }
}

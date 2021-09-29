package geekbarains.material.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import geekbarains.material.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setTheme()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commitNow()
        }
    }

    private fun setTheme() {
        var theme = R.style.AppEarthTheme
        if (getPreferences(Context.MODE_PRIVATE).getBoolean(SettingsFragment.EARTH_THEME, false)) {
            theme=R.style.AppEarthTheme
        }
        if (getPreferences(Context.MODE_PRIVATE).getBoolean(SettingsFragment.MOON_THEME, false)) {
            theme=R.style.AppMoonTheme
        }
        if (getPreferences(Context.MODE_PRIVATE).getBoolean(SettingsFragment.MARS_THEME, false)) {
            theme=R.style.AppMarsTheme
        }
        setTheme(theme)
    }
}

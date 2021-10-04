package geekbarains.material.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import geekbarains.material.R
import geekbarains.material.ui.fragments.SettingsFragment

open class BaseActivity : AppCompatActivity() {
    protected fun setTheme() {
        val pref=getSharedPreferences(SettingsFragment.MY_SETTINGS, Context.MODE_PRIVATE)

        var theme = R.style.AppEarthTheme
        if (pref.getBoolean(SettingsFragment.EARTH_THEME, false)) theme= R.style.AppEarthTheme
        if (pref.getBoolean(SettingsFragment.MOON_THEME, false)) theme= R.style.AppMoonTheme
        if (pref.getBoolean(SettingsFragment.MARS_THEME, false)) theme= R.style.AppMarsTheme

        setTheme(theme)
    }
}
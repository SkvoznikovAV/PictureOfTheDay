package geekbarains.material.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import geekbarains.material.R
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    private fun changeTheme(idTheme : Int){
        activity?.let { reqActivity ->
            val preferences = reqActivity.getPreferences(Context.MODE_PRIVATE)
            val editor = preferences.edit()

            when (idTheme){
                R.id.chipEarthTheme -> {
                    editor.putBoolean(EARTH_THEME, true)
                    editor.putBoolean(MOON_THEME, false)
                    editor.putBoolean(MARS_THEME, false)
                }
                R.id.chipMoonTheme -> {
                    editor.putBoolean(EARTH_THEME, false)
                    editor.putBoolean(MOON_THEME, true)
                    editor.putBoolean(MARS_THEME, false)
                }
                R.id.chipMarsTheme -> {
                    editor.putBoolean(EARTH_THEME, false)
                    editor.putBoolean(MOON_THEME, false)
                    editor.putBoolean(MARS_THEME, true)
                }
                else -> return
            }

            editor.apply()
            reqActivity.recreate()
        }
    }

    private fun readTheme() {
        activity?.let {
            if (it.getPreferences(Context.MODE_PRIVATE).getBoolean(EARTH_THEME, false)) chipEarthTheme.isChecked = true
            if (it.getPreferences(Context.MODE_PRIVATE).getBoolean(MOON_THEME, false)) chipMoonTheme.isChecked = true
            if (it.getPreferences(Context.MODE_PRIVATE).getBoolean(MARS_THEME, false)) chipMarsTheme.isChecked = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readTheme()

        chipGroupThemes.setOnCheckedChangeListener { _, checkedId ->
            chipGroupThemes.findViewById<Chip>(checkedId)?.let {
                changeTheme(it.id)
            }
        }
    }

    companion object{
        const val EARTH_THEME = "EARTH_THEME"
        const val MOON_THEME = "MOON_THEME"
        const val MARS_THEME = "MARS_THEME"
    }
}

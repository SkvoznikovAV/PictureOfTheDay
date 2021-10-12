package geekbarains.material.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.transition.Slide
import androidx.transition.TransitionManager
import geekbarains.material.R
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_write_dev.setOnClickListener {
            TransitionManager.beginDelayedTransition(about_container, Slide(Gravity.START))
            msg_dev.visibility = View.VISIBLE
            btn_send_develop.visibility = View.VISIBLE
        }
    }
}
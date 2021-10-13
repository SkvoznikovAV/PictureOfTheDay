package geekbarains.material.notes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import geekbarains.material.R
import kotlinx.android.synthetic.main.dialog_note.*

class NoteDialogFragment(private var btnClickListener : onClickListener) : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_note, container, false)
        isCancelable = false
        return view
    }

    override fun onStart() {
        super.onStart()

        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.window?.setLayout(width, height)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_note_ok.setOnClickListener {
            btnClickListener.onButtonOkClick(note_edit.text.toString())
            dismiss()
        }

        btn_note_cancel.setOnClickListener {
            dismiss()
        }
    }

    interface onClickListener{
        fun onButtonOkClick(txt: String)
    }
}
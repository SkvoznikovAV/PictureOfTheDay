package geekbarains.material.notes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import geekbarains.material.R
import kotlinx.android.synthetic.main.dialog_note.*

class NoteDialogFragment(private var btnClickListener : OnClickListener) : DialogFragment() {
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

        arguments?.let{
            note_edit.setText(it.getString(TXT_NOTE))
        }

        btn_note_ok.setOnClickListener {
            btnClickListener.onButtonOkClick(note_edit.text.toString())
            dismiss()
        }

        btn_note_cancel.setOnClickListener {
            dismiss()
        }
    }

    interface OnClickListener{
        fun onButtonOkClick(txt: String)
    }

    companion object{
        private const val TXT_NOTE="TXT_NOTE"

        fun newInstance(btnClickListener : OnClickListener, txt: String = ""): NoteDialogFragment{
            val args = Bundle()
            args.putString(TXT_NOTE,txt)

            val instance=NoteDialogFragment(btnClickListener)
            instance.arguments=args

            return instance
        }
    }
}
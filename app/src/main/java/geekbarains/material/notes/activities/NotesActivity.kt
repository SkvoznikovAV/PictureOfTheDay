package geekbarains.material.notes.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import geekbarains.material.R
import geekbarains.material.databinding.ActivityNotesBinding
import geekbarains.material.notes.adapters.NotesAdapter
import geekbarains.material.notes.entities.Note
import geekbarains.material.notes.ui.NoteDialogFragment
import geekbarains.material.ui.activities.BaseActivity
import java.util.*

class NotesActivity: BaseActivity() {
    lateinit var binding : ActivityNotesBinding
    private val adapter = NotesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme()

        super.onCreate(savedInstanceState)
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() = with (binding) {
        notesRecycleView.layoutManager = LinearLayoutManager(this@NotesActivity)
        notesRecycleView.adapter = adapter

        adapter.addNote(Note("Заметка 1", Date()))
        adapter.addNote(Note("Заметка 2", Date()))
        adapter.addNote(Note("Заметка 3", Date()))
        adapter.addNote(Note("Заметка 4", Date()))

        btnAddNote.setOnClickListener {
            NoteDialogFragment(object : NoteDialogFragment.onClickListener {
                override fun onButtonOkClick(txt: String) {
                    adapter.addNote(Note(txt, Date()))
                }
            }).show(supportFragmentManager, null)
        }
    }
}
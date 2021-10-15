package geekbarains.material.notes.activities

import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import geekbarains.material.databinding.ActivityNotesBinding
import geekbarains.material.notes.adapters.NotesAdapter
import geekbarains.material.notes.entities.Note
import geekbarains.material.notes.ui.ItemTouchHelperCallback
import geekbarains.material.notes.ui.NoteDialogFragment
import geekbarains.material.ui.activities.BaseActivity
import java.util.*

class NotesActivity: BaseActivity() {
    private lateinit var binding : ActivityNotesBinding
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

        ItemTouchHelper(ItemTouchHelperCallback(adapter))
            .attachToRecyclerView(notesRecycleView)

        adapter.addNote(Note("Заметка 1", Date()))
        adapter.addNote(Note("Заметка 2", Date()))
        adapter.addNote(Note("Заметка 3", Date()))
        adapter.addNote(Note("Заметка 4", Date()))
        adapter.addNote(Note("Заметка 5", Date()))

        adapter.setOnItemClickListener(object: NotesAdapter.OnItemClickListener{
            override fun onItemClick(note: Note, position: Int) {
                NoteDialogFragment.newInstance(object : NoteDialogFragment.OnClickListener {
                    override fun onButtonOkClick(txt: String) {
                        adapter.changeNote(txt,position)
                    }
                },note.title).show(supportFragmentManager,null)
            }
            override fun onItemRemoved(position: Int) {
                adapter.delNote(position)
            }
        })

        btnAddNote.setOnClickListener {
            NoteDialogFragment(object : NoteDialogFragment.OnClickListener {
                override fun onButtonOkClick(txt: String) {
                    adapter.addNote(Note(txt, Date()))
                }
            }).show(supportFragmentManager, null)
        }
    }
}
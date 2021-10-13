package geekbarains.material.notes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import geekbarains.material.R
import geekbarains.material.databinding.ItemNoteBinding
import geekbarains.material.notes.entities.Note
import java.text.SimpleDateFormat

class NotesAdapter: RecyclerView.Adapter<NotesAdapter.NotesHolder>() {
    private val notes = ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note,parent,false)
        return NotesHolder(view)
    }

    override fun onBindViewHolder(holder: NotesHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class NotesHolder(item: View): RecyclerView.ViewHolder(item){
        val binding = ItemNoteBinding.bind(item)

        fun bind(note: Note) = with (binding){
            noteTitle.text = note.title;
            noteDate.text = SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(note.date)
        }
    }

    fun addNote(note: Note){
        notes.add(note)
        notifyDataSetChanged()
    }
}
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
    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note,parent,false)
        return NotesHolder(view)
    }

    override fun onBindViewHolder(holder: NotesHolder, position: Int) {
        holder.bind(notes[position], onItemClickListener, position)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class NotesHolder(item: View): RecyclerView.ViewHolder(item){
        val binding = ItemNoteBinding.bind(item)

        fun bind(note: Note, onItemClickListener: OnItemClickListener?, position: Int) = with (binding){
            noteTitle.text = note.title;
            noteDate.text = SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(note.date)

            delNote.setOnClickListener{
                onItemClickListener?.onItemRemoved(position)
            }

            onItemClickListener?.let{
                itemView.setOnClickListener{
                    onItemClickListener.onItemClick(note, position)
                }
            }
        }
    }

    fun changeNote(txt: String, position: Int){
        notes.set(position, Note(txt))
        notifyItemChanged(position)
    }

    fun addNote(note: Note){
        notes.add(note)
        notifyItemInserted(notes.size-1)
    }

    fun delNote(position: Int){
        notes.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, getItemCount());
    }

    interface OnItemClickListener{
        fun onItemClick(note: Note, position: Int)
        fun onItemRemoved(position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }
}
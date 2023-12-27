package com.example.todolistapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.databinding.NoteItemBinding
import com.example.todolistapp.fragment.NoteFragmentDirections
import com.example.todolistapp.room.NoteModel


class NoteAdapter(private var noteList: List<NoteModel>) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(private val itemBinding: NoteItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(model: NoteModel) {
            itemBinding.note = model
            itemBinding.noteCardView.setOnClickListener {
                val action = NoteFragmentDirections.actionNoteFragmentToUpdateFragment(model)
                Navigation.findNavController(it).navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val noteItemBinding = NoteItemBinding.inflate(layoutInflater, parent, false)
        return NoteViewHolder(noteItemBinding)
    }

    override fun getItemCount(): Int = noteList.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        holder.bind(note)
    }

    fun updateList(newList: List<NoteModel>) {
        noteList = newList
        notifyDataSetChanged()
    }
}

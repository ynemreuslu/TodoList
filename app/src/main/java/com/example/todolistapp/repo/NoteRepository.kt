package com.example.todolistapp.repo


import com.example.todolistapp.room.NoteDao
import com.example.todolistapp.room.NoteModel
import javax.inject.Inject

class NoteRepository @Inject constructor(private val dao: NoteDao) {

    suspend fun insertNote(note: NoteModel) {
        dao.insertNote(note)
    }

    suspend fun deleteNote(id : Int) {
        dao.deleteNote(id)
    }

    suspend fun updateNote(note: NoteModel) {
        dao.updateNote(note)
    }

    suspend fun getNoteAll(): List<NoteModel> {
        return dao.getAllNote()
    }

}
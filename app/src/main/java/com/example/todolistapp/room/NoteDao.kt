package com.example.todolistapp.room


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {

    @Insert
    suspend fun insertNote(noteModel: NoteModel)

    @Query("Delete  FROM note_table WHERE id = :id")
    suspend fun deleteNote(id: Int)

    @Update
    suspend fun updateNote(noteModel: NoteModel)

    @Query("SELECT * FROM note_table")
    suspend fun getAllNote(): List<NoteModel>
}
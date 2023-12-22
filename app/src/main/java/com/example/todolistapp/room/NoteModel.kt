package com.example.todolistapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity("note_table")
data class NoteModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val note: String,
    val date: String
) : Serializable
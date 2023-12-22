package com.example.todolistapp.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.repo.NoteRepository
import com.example.todolistapp.room.NoteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository, application: Application
) : AndroidViewModel(application) {

    val noteList = MutableLiveData<List<NoteModel>>()

    init {
        getNoteAll()
    }

    private fun getNoteAll() {
        viewModelScope.launch {
            noteList.postValue(noteRepository.getNoteAll())
        }
    }

    fun insertNote(note: NoteModel) {
        viewModelScope.launch {
            noteRepository.insertNote(note)
            getNoteAll()
        }
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch {
            noteRepository.deleteNote(id)
        }
    }

    fun updateNote(note: NoteModel) {
        viewModelScope.launch {
            noteRepository.updateNote(note)
        }
    }

}
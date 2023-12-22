package com.example.todolistapp.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todolistapp.R
import com.example.todolistapp.databinding.FragmentAddBinding
import com.example.todolistapp.room.NoteModel
import com.example.todolistapp.vm.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date


@AndroidEntryPoint
class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private val viewModel: NoteViewModel by viewModels()
    private val textHistory = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_add, container, false)
        val date = Date()
        val noteDate: CharSequence = DateFormat.format("MMMM d HH:mm", date.time)
        binding.addNoteFragmentDateTextView.text = noteDate

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAddButton()
        setupNoteEditText()
        setupBackButton()
    }

    private fun setupAddButton() {
        binding.addNoteFragmentAddButton.setOnClickListener {
            isEmpty()
        }
    }

    private fun isEmpty() {
        if (binding.addNoteFragmentNoteEditText.text.isEmpty() && binding.addNoteFragmentTitleEditText.text.isEmpty()) {

        } else {
            saveNote()
            navigateToNoteFragment()
        }

    }

    private fun saveNote() {
        val title = binding.addNoteFragmentTitleEditText.text.toString()
        val note = binding.addNoteFragmentNoteEditText.text.toString()
        val date = Date()

        val noteDate: CharSequence = DateFormat.format("MMMM d, yyyy HH:mm", date.time)

        val noteModel = NoteModel(title = title, note = note, date = noteDate.toString())
        viewModel.insertNote(noteModel)


    }

    private fun navigateToNoteFragment() {
        findNavController().navigate(R.id.action_addFragment_to_noteFragment)
    }

    private fun setupNoteEditText() {
        binding.addNoteFragmentNoteEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.isNotEmpty() == true) {
                    enableUndoButton()
                    disableForwardButton()
                } else {
                    disableUndoButton()
                    enableForwardButton()
                }

                val characterCount = p0?.length ?: 0
                updateCharacterCount(characterCount)
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }

    private fun enableUndoButton() {
        binding.addNoteFragmentUndoTextButton.isEnabled = true
        binding.addNoteFragmentUndoTextButton.setImageResource(R.drawable.undo)
        binding.addNoteFragmentUndoTextButton.setOnClickListener {
            textHistory.add(binding.addNoteFragmentNoteEditText.text.toString())
            binding.addNoteFragmentNoteEditText.text.clear()
        }
    }

    private fun disableUndoButton() {
        binding.addNoteFragmentUndoTextButton.isEnabled = false
        binding.addNoteFragmentUndoTextButton.setImageResource(R.drawable.undo_gone)
    }

    private fun updateCharacterCount(count: Int) {
        binding.addNoteFragmentCharacterCountTextView.text = "| $count karakter"
    }

    private fun setupBackButton() {
        binding.addNoteFragmentBackButton.setOnClickListener {
            navigateToNoteFragment()
        }
    }

    private fun enableForwardButton() {
        binding.addNoteFragmentForwardTextButton.isEnabled = true
        binding.addNoteFragmentForwardTextButton.setImageResource(R.drawable.shortcut)
        binding.addNoteFragmentForwardTextButton.setOnClickListener {
            if (textHistory.isNotEmpty()) {
                val lastText = textHistory.removeAt(textHistory.size - 1)
                binding.addNoteFragmentNoteEditText.setText(lastText)
            }
        }
    }

    private fun disableForwardButton() {
        binding.addNoteFragmentForwardTextButton.isEnabled = false
        binding.addNoteFragmentForwardTextButton.setImageResource(R.drawable.shortcut_gone)
    }
}

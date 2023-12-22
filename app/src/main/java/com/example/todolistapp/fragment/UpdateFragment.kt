package com.example.todolistapp.fragment


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolistapp.R
import com.example.todolistapp.databinding.FragmentUpdateBinding
import com.example.todolistapp.room.NoteModel
import com.example.todolistapp.vm.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date


@AndroidEntryPoint
class UpdateFragment : Fragment() {

    private lateinit var binding: FragmentUpdateBinding

    private val viewModel: NoteViewModel by viewModels()
    private val args by navArgs<UpdateFragmentArgs>()
    private val textHistory = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_update, container, false)
        setupNoteData()
        updateCharacterCount(binding.updateNoteFragmentNoteEditText.text.length)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNoteDate()
        setupCharacterCount()
        setupButtonListeners()
    }


    private fun setupNoteDate() {
        val currentDate = Date()
        val noteDate: CharSequence = DateFormat.format("MMMM d HH:mm", currentDate.time)
        binding.updateNoteFragmenDate.text = noteDate
    }

    private fun setupNoteData() {
        val note = args.note
        binding.updateNoteFragmentTitleEditText.setText(note.title)
        binding.updateNoteFragmentNoteEditText.setText(note.note)
    }

    private fun setupCharacterCount() {
        binding.updateNoteFragmentNoteEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val characterCount = p0?.length ?: 0
                updateCharacterCount(characterCount)
                updateButtonVisibility(p0!!.isNotEmpty())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.updateNoteFragmentTitleEditText.addTextChangedListener {
            binding.updateFragmentUpdateButton.visibility = View.VISIBLE
        }
    }

    private fun updateCharacterCount(count: Int) {
        binding.updateNoteFragmentCharacterCountTextView.text = "| $count karakter"
    }

    private fun updateButtonVisibility(noteNotEmpty: Boolean) {
        binding.updateFragmentUpdateButton.visibility = View.VISIBLE
        binding.updateFragmentUndoTextButton.visibility = View.VISIBLE
        binding.updateFragmentForwardTextButton.visibility = View.VISIBLE

        if (noteNotEmpty) {
            enableUndoButton()
            disableForwardButton()
        } else {
            enableForwardButton()
            disableUndoButton()
        }
    }

    private fun setupButtonListeners() {
        binding.updateFragmentUpdateButton.setOnClickListener {
            isEmpty()
        }

        binding.updateNoteFragmentDeleteButton.setOnClickListener {
            navigateToDeleteBottom()
        }
    }

    private fun isEmpty() {
        if (binding.updateNoteFragmentNoteEditText.text.isEmpty() && binding.updateNoteFragmentTitleEditText.text.isEmpty()) {
        } else {
            updateNote()
            navigateToNoteFragment()
        }
    }

    private fun enableUndoButton() {
        binding.updateFragmentUndoTextButton.isEnabled = true
        binding.updateFragmentUndoTextButton.setImageResource(R.drawable.undo)
        binding.updateFragmentUndoTextButton.setOnClickListener {
            textHistory.add(binding.updateNoteFragmentNoteEditText.text.toString())
            binding.updateNoteFragmentNoteEditText.text.clear()
        }
    }

    private fun disableUndoButton() {
        binding.updateFragmentUndoTextButton.isEnabled = false
        binding.updateFragmentUndoTextButton.setImageResource(R.drawable.undo_gone)
    }

    private fun enableForwardButton() {
        binding.updateFragmentForwardTextButton.isEnabled = true
        binding.updateFragmentForwardTextButton.setImageResource(R.drawable.shortcut)
        binding.updateFragmentForwardTextButton.setOnClickListener {
            if (textHistory.isNotEmpty()) {
                val lastText = textHistory.removeAt(textHistory.size - 1)
                binding.updateNoteFragmentNoteEditText.setText(lastText)
            }
        }
    }

    private fun disableForwardButton() {
        binding.updateFragmentForwardTextButton.isEnabled = false
        binding.updateFragmentForwardTextButton.setImageResource(R.drawable.shortcut_gone)
    }

    private fun updateNote() {
        val noteDescription = binding.updateNoteFragmentNoteEditText.text.toString()
        val title = binding.updateNoteFragmentTitleEditText.text.toString()
        val currentDate = Date()
        val noteDate: CharSequence = DateFormat.format("MMMM d, yyyy HH:mm", currentDate.time)

        val note = NoteModel(args.note.id, title, noteDescription, noteDate.toString())
        viewModel.updateNote(note)
    }

    private fun deleteNote() {

        viewModel.deleteNote(args.note.id)
    }

    private fun navigateToNoteFragment() {
        val action = UpdateFragmentDirections.actionUpdateFragmentToNoteFragment()
        findNavController().navigate(action)
    }

    private fun navigateToDeleteBottom() {
        val action =
            UpdateFragmentDirections.actionUpdateFragmentToDeleteBottomSheetDiolog2(args.note)
        findNavController().navigate(action)
    }


}

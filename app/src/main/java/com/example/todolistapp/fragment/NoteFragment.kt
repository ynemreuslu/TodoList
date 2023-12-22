package com.example.todolistapp.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todolistapp.R
import com.example.todolistapp.adapter.NoteAdapter
import com.example.todolistapp.databinding.FragmentNoteBinding
import com.example.todolistapp.room.NoteModel
import com.example.todolistapp.vm.NoteViewModel
import com.google.android.material.search.SearchView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private lateinit var binding: FragmentNoteBinding
    private val viewModel: NoteViewModel by viewModels()
    private lateinit var adapter: NoteAdapter
    private lateinit var noteList: List<NoteModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note, container, false)
        observeNoteList()
        binding.lifecycleOwner = this
        setupSearch()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAddActionButton()
        displayNotes()
        visibilityButton()
    }

    private fun observeNoteList() {
        viewModel.noteList.observe(viewLifecycleOwner) {
            noteList = it
            initializeAdapter()
        }
    }

    private fun initializeAdapter() {
        adapter = NoteAdapter(noteList)
        binding.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun setupAddActionButton() {
        binding.noteFragmentAddActionButton.setOnClickListener {
            navigateToAddFragment()
        }
    }

    private fun displayNotes() {
        viewModel.noteList.observe(viewLifecycleOwner) {
            noteList = it
            binding.apply {
                if (noteList.isEmpty()) {
                    showNoNotesSnackbar()
                } else {
                    setUpRecyclerView()
                }
            }
        }
    }

    private fun showNoNotesSnackbar() {
        val snackbar = Snackbar.make(
            requireView(),
            getString(R.string.no_not),
            Snackbar.LENGTH_SHORT
        )
        snackbar.setBackgroundTint(
            ContextCompat.getColor(requireContext(), R.color.orange)
        )
        snackbar.setTextColor(
            ContextCompat.getColor(requireContext(), R.color.white)
        )
        snackbar.show()
    }

    private fun navigateToAddFragment() {
        findNavController().navigate(R.id.action_noteFragment_to_addFragment)
    }

    private fun setUpRecyclerView() {
        binding.noteFragmentRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun setupSearch() {
        binding.searchView.setupWithSearchBar(binding.searchBar)

        binding.searchView.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                performSearch(charSequence.toString())
            }

            override fun afterTextChanged(editable: Editable?) {}
        })
    }

    private fun visibilityButton() {
        binding.searchView.addTransitionListener { searchView, previousState, newState ->
            when (newState) {
                SearchView.TransitionState.HIDDEN -> {
                    binding.noteFragmentAddActionButton.visibility = View.VISIBLE
                }

                SearchView.TransitionState.SHOWN -> {
                    binding.noteFragmentAddActionButton.visibility = View.GONE
                }

                else -> {}
            }
        }
    }

    private fun performSearch(query: String) {
        val filteredList = noteList.filter { noteItem ->
            noteItem.note.contains(query, ignoreCase = true) ||
                    noteItem.title.contains(query, ignoreCase = true)
        }
        adapter.updateList(filteredList)
    }
}




package com.example.todolistapp.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolistapp.R
import com.example.todolistapp.databinding.DeleteBottomSheetBinding
import com.example.todolistapp.vm.NoteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteBottomSheetDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DeleteBottomSheetBinding
    val args: DeleteBottomSheetDialogArgs by navArgs()
    val viewModel: NoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DeleteBottomSheetBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtonClickListeners()
    }

    private fun setupButtonClickListeners() {
        binding.yesButton.setOnClickListener {
            handleYesButtonClick()
        }

        binding.noButton.setOnClickListener {
            handleNoButtonClick()
        }
    }

    private fun handleYesButtonClick() {
        viewModel.deleteNote(args.noteId.id)
        findNavController().navigate(R.id.action_deleteBottomSheetDiolog_to_noteFragment)
    }

    private fun handleNoButtonClick() {
        findNavController().popBackStack()
    }


}

package com.example.todolistapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolistapp.databinding.DeleteBottomSheetBinding
import com.example.todolistapp.fragment.UpdateFragmentDirections
import com.example.todolistapp.vm.NoteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteBottomSheetDialog : BottomSheetDialogFragment() {
    private var _binding: DeleteBottomSheetBinding? = null
    val args: DeleteBottomSheetDialogArgs by navArgs<DeleteBottomSheetDialogArgs>()
    val viewModel: NoteViewModel by viewModels()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DeleteBottomSheetBinding.inflate(inflater, container, false)
        val view = binding.root
        val id = args.noteId.id
        Toast.makeText(requireContext(),"${id}",Toast.LENGTH_LONG).show()
        binding.yesButton.setOnClickListener {
            viewModel.deleteNote(id)
            findNavController().navigate(R.id.action_deleteBottomSheetDiolog_to_noteFragment)


        }

        binding.noButton.setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
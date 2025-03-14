package dev.androidbroadcast.notesapp.repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dev.androidbroadcast.notesapp.R
import dev.androidbroadcast.notesapp.data.Note
import dev.androidbroadcast.notesapp.data.NoteDatabase
import dev.androidbroadcast.notesapp.databinding.FragmentNoteDetailsBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteDetailsFragment : Fragment() {
    private var _binding: FragmentNoteDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NoteViewModel
    private var noteId: Int = -1 // Default значение

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получаем noteId из аргументов
        arguments?.let {
            noteId = it.getInt("noteId", -1)
        }

        // Инициализация ViewModel
        val repository = NoteRepository(NoteDatabase.getDatabase(requireContext()).noteDao())
        val factory = NoteViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)

        // Загружаем заметку по ID
        viewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            val note = notes.find { it.id == noteId }
            note?.let {
                binding.textViewName.text = it.name
                binding.textViewDate.text = formatDate(it.date.toLong())
                binding.textViewContent.text = it.text
            }
        }

        // Кнопка редактирования
        binding.buttonEdit.setOnClickListener {
            val bundle = Bundle().apply { putInt("noteId", noteId) }
            findNavController().navigate(R.id.action_noteDetailsFragment_to_editNoteFragment, bundle)
        }

        // Кнопка удаления
        binding.buttonDelete.setOnClickListener {
            viewModel.delete(Note(noteId, binding.textViewName.text.toString(), binding.textViewDate.text.toString(), binding.textViewContent.text.toString()))
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

}


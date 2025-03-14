package dev.androidbroadcast.notesapp.repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dev.androidbroadcast.notesapp.data.Note
import dev.androidbroadcast.notesapp.data.NoteDatabase
import dev.androidbroadcast.notesapp.databinding.FragmentNoteEditBinding

class NoteEditFragment : Fragment() {
    private var _binding: FragmentNoteEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NoteViewModel
    private var noteId: Int = -1
    private var noteName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteEditBinding.inflate(inflater, container, false)
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

        // Загружаем существующую заметку, если она есть
        if (noteId != -1) {
            viewModel.allNotes.observe(viewLifecycleOwner) { notes ->
                val note = notes.find { it.id == noteId }
                note?.let {
                    noteName = it.name // Сохраняем имя, так как менять его нельзя
                    binding.editTextContent.setText(it.text) // Заполняем только текст
                }
            }
        }

        // Кнопка "Сохранить"
        binding.buttonSave.setOnClickListener {
            val newText = binding.editTextContent.text.toString()
            if (newText.isNotEmpty()) {
                val updatedNote = Note(
                    noteId,
                    noteName, // Используем старое название
                    System.currentTimeMillis().toString(), // Обновляем дату
                    newText
                )
                viewModel.update(updatedNote)
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Поле заметки не может быть пустым", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.example.bottomnavigationapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomnavigationapp.databinding.FragmentNoteListBinding


class NoteListFragment : Fragment() {
    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!

    private var adapter: CustomAdapter? = null
    private var noteList = arrayListOf<Note>()
    private var note: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newList = arguments?.getParcelableArrayList<Note>("newList")
        if (newList != null) {
            noteList = newList
        }
        val newPosition = arguments?.getInt("positionNew")
        if (newPosition != null) {
            createAdapter()
        }

        binding.saveBT.setOnClickListener {
            val number = noteList.size + 1
            val text = binding.noteET.text.toString()
            note = Note(number, text, false)
            noteList.add(note!!)
            binding.noteET.text.clear()
            createAdapter()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun createAdapter() {
        adapter = CustomAdapter(noteList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.setHasFixedSize(true)
        adapter?.notifyDataSetChanged()
        adapter?.setOnNoteClickListener(object : CustomAdapter.OnNoteClickListener {
            override fun onNoteClick(note: Note, position: Int) {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                val inflater = this@NoteListFragment.layoutInflater
                val dialogView = inflater.inflate(R.layout.edit_dialog, null)
                val editText = dialogView.findViewById<EditText>(R.id.editNoteET)
                dialogBuilder.setView(dialogView)
                dialogBuilder.setTitle("Обновить заметку")
                dialogBuilder.setPositiveButton("Обновить"){_, _ ->
                    val updateNote = editText.text.toString()
                    if (updateNote.trim() != ""){
                        note.noteText = updateNote
                        adapter?.notifyDataSetChanged()
                    }
                }
                dialogBuilder.setNegativeButton("Отмена"){_, _ ->}
                dialogBuilder.create().show()

                binding.recyclerView.findViewById<ImageView>(R.id.deleteIV).setOnClickListener {
                    noteList.removeAt(position)
                    adapter?.notifyDataSetChanged()
                }
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}
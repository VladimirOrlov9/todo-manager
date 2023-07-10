package com.example.todo_manager.ui.fragment.todoscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.todo_manager.R
import com.example.todo_manager.databinding.FragmentTodoScreenBinding

class TodoScreenFragment : Fragment() {

    private var _binding: FragmentTodoScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoScreenBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initImportanceSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.importance_variants,
            android.R.layout.simple_spinner_item
        ).also { adapter ->

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.importanceSpinner.adapter = adapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setupWithNavController(findNavController())
        initImportanceSpinner()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
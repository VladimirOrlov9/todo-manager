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
import com.example.todo_manager.domain.model.Importance
import com.example.todo_manager.domain.model.TodoItem
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.UUID

class TodoScreenFragment : Fragment() {

    private var _binding: FragmentTodoScreenBinding? = null
    private val binding get() = _binding!!

    private val vm by viewModel<TodoScreenViewModel>()

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

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.save_action -> {
                    val todo: TodoItem = collectTodoInfoFromUI()
                    vm.createNewTodoItem(todo)
                    true
                }
                else -> false
            }
        }
    }

    private fun collectTodoInfoFromUI(): TodoItem {
        val creationDate = System.currentTimeMillis()
        val todoText = binding.todoEdittext.text.toString()
        val importance = Importance.values()[binding.importanceSpinner.selectedItemPosition]
        val deadlineDate = when (binding.deadlineSwitch.isChecked) {
            true -> {
                // TODO: convert date (12.12.1234) to millis
                binding.deadlineDateTextview.text.toString().toLong()
            }
            else -> null
        }
        val id = UUID.randomUUID().toString()

        return TodoItem(
            id = id,
            description = todoText,
            importance = importance,
            deadline = deadlineDate,
            creationDate = creationDate
        )

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
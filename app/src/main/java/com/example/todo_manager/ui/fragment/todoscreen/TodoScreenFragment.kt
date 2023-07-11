package com.example.todo_manager.ui.fragment.todoscreen

import android.icu.text.SimpleDateFormat
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
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

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
        initDeadlineCalendar()

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

        vm.dateInMillis.observe(viewLifecycleOwner) { dateInMillis ->
            binding.deadlineDateTextview.text = getDateFormMillis(dateInMillis)
        }
    }

    private fun initDeadlineCalendar() {
        binding.deadlineSwitch.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> binding.deadlineDateTextview.visibility = View.VISIBLE
                false -> binding.deadlineDateTextview.visibility = View.GONE
            }
        }
        binding.deadlineDateTextview.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
                .apply {
                    addOnPositiveButtonClickListener {
                        vm.setNewDate(it)
                    }
                }
            datePicker.show(parentFragmentManager, "deadline_calendar")
        }
    }

    private fun getDateFormMillis(millis: Long): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(millis)
    }

    private fun collectTodoInfoFromUI(): TodoItem {
        val creationDate = System.currentTimeMillis()
        val todoText = binding.todoEdittext.text.toString()
        val importance = Importance.values()[binding.importanceSpinner.selectedItemPosition]
        val deadlineDate = when (binding.deadlineSwitch.isChecked) {
            true -> {
                vm.dateInMillis.value
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
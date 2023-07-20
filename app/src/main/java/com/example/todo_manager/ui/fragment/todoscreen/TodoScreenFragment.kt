package com.example.todo_manager.ui.fragment.todoscreen

import android.icu.text.SimpleDateFormat
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.domain.getDateFromMillis
import com.example.todo_manager.R
import com.example.todo_manager.databinding.FragmentTodoScreenBinding
import com.example.todo_manager.domain.model.Importance
import com.example.todo_manager.domain.model.TodoItem
import com.example.todo_manager.ui.fragment.mainscreen.TODO_BUNDLE
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

class TodoScreenFragment : Fragment() {

    private var _binding: FragmentTodoScreenBinding? = null
    private val binding get() = _binding!!

    private val vm by viewModel<TodoScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.parcelable<TodoItem>(TODO_BUNDLE)?.let {
            vm.setTodo(it)
        }
    }

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
        initExistingData()

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.save_action -> {
                    saveTodoInfoFromUI()
                    true
                }
                else -> false
            }
        }

        vm.dateInMillis.observe(viewLifecycleOwner) { dateInMillis ->
            binding.deadlineDateTextview.text = getDateFromMillis(dateInMillis)
        }

        binding.deleteButton.setOnClickListener {
            vm.deleteTodo()
        }

        vm.successDBOperation.observe(viewLifecycleOwner) {
            if (it)
                findNavController().popBackStack()
        }
    }

    private fun initExistingData() {
        vm.getTodo()?.let {  todoItem ->
            binding.apply {
                todoEdittext.setText(todoItem.description)
                importanceSpinner.setSelection(todoItem.importance.ordinal)

                val deadline = todoItem.deadline
                if (deadline != null) {
                    deadlineSwitch.isChecked = true
                    vm.setNewDate(deadline)
                }

                deleteButton.isEnabled = true
            }
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
                .setSelection(vm.dateInMillis.value)
                .build()
                .apply {
                    addOnPositiveButtonClickListener {
                        vm.setNewDate(it)
                    }
                }
            datePicker.show(parentFragmentManager, "deadline_calendar")
        }
    }

    private fun saveTodoInfoFromUI() {
        val todoText = binding.todoEdittext.text.toString()
        val importance = Importance.values()[binding.importanceSpinner.selectedItemPosition]
        val deadlineDate = when (binding.deadlineSwitch.isChecked) {
            true -> vm.dateInMillis.value
            else -> null
        }

        vm.createNewTodoItem(
            description = todoText,
            importance = importance,
            deadline = deadlineDate
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
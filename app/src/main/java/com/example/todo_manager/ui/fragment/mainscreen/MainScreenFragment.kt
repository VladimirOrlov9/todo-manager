package com.example.todo_manager.ui.fragment.mainscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo_manager.R
import com.example.todo_manager.databinding.FragmentMainScreenBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

const val TODO_BUNDLE = "todo_bundle"
const val WAS_FILTERED_FLAG = "was_filtered_flag"

class MainScreenFragment : Fragment() {

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!

    private val vm by viewModel<MainScreenViewModel>()
    private lateinit var adapter: TodoListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setupWithNavController(findNavController())
        initRecyclerView()

        vm.todoList.observe(viewLifecycleOwner) { newList ->
            adapter.submitListWithFilterApply(newList)
        }
        binding.addFab.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreenFragment_to_todoScreenFragment)
        }
        binding.toolbar.setOnMenuItemClickListener {  menuItem ->
            when(menuItem.itemId) {
                R.id.visibility -> {
                    adapter.changeVisibility()
                    vm.invIsFiltered()
                    true
                }
                else -> false
            }
        }
    }

    private fun initRecyclerView() {
        adapter = TodoListAdapter(
            todoInfoClickEvent = { todoItem ->
                val bundle = Bundle().apply {
                    putParcelable(TODO_BUNDLE, todoItem)
                }
                findNavController().navigate(
                    R.id.action_mainScreenFragment_to_todoScreenFragment,
                    bundle
                )
            },
            todoCheckBoxStatusChangedEvent = { id, isChecked ->
                vm.updateTodoStatus(id, isChecked)
            },
            newTodoClickEvent = {
                // TODO: navigate to todo creation
                findNavController().navigate(R.id.action_mainScreenFragment_to_todoScreenFragment)
            })
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        if (vm.getIsFiltered())
            adapter.changeVisibility()
    }

    override fun onStart() {
        super.onStart()

        vm.loadTodoList()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
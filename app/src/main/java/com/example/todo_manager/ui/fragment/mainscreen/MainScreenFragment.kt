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


class MainScreenFragment : Fragment() {

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!

    private val vm by viewModel<MainScreenViewModel>()
    private lateinit var adapter: TodoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm.loadTodoList()
    }

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
            adapter.submitList(newList)
        }
    }

    private fun initRecyclerView() {
        adapter = TodoListAdapter { id ->
            // TODO: navigate to todo details
        }
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
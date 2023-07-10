package com.example.todo_manager.ui.fragment.mainscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo_manager.R
import com.example.todo_manager.databinding.FragmentMainScreenBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

const val TODO_ID_BUNDLE = "todo_id_bundle"

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
            adapter.submitList(newList)
        }
        binding.addFab.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreenFragment_to_todoScreenFragment)
        }
    }

    private fun initRecyclerView() {
        adapter = TodoListAdapter(
            todoClickEvent = { id ->
                val bundle = Bundle().apply {
                    putString(TODO_ID_BUNDLE, id)
                }
                findNavController().navigate(
                    R.id.action_mainScreenFragment_to_todoScreenFragment,
                    bundle
                )
            },
            newTodoClickEvent = {
                // TODO: navigate to todo creation
                findNavController().navigate(R.id.action_mainScreenFragment_to_todoScreenFragment)
            })
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
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
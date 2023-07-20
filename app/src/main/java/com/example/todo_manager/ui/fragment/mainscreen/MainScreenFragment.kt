package com.example.todo_manager.ui.fragment.mainscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo_manager.R
import com.example.todo_manager.databinding.FragmentMainScreenBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

const val TODO_BUNDLE = "todo_bundle"

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
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.visibility -> {
                    vm.invIsFiltered()

                    true
                }
                else -> false
            }
        }

        vm.completed.observe(viewLifecycleOwner) {
            binding.toolbar.subtitle = getCompletedObjectsString(it)
        }

        vm.isFiltered.observe(viewLifecycleOwner) { isFiltered ->
            if (isFiltered)
                binding.toolbar.menu.findItem(R.id.visibility).setIcon(Visibility.INVISIBLE.resId)
            else
                binding.toolbar.menu.findItem(R.id.visibility).setIcon(Visibility.VISIBLE.resId)
        }
    }

    private fun getCompletedObjectsString(completedSum: Int) =
        getString(R.string.completed) + ": " + completedSum

    private fun initRecyclerView() {
        adapter = TodoListAdapter(
            hideCompletedFlag = vm.isFiltered.value ?: false,
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
                findNavController().navigate(R.id.action_mainScreenFragment_to_todoScreenFragment)
            },
            swipeDeleteEvent = {
                vm.deleteTodo(it)
            })

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
        val callback = TodoTouchHelperCallback(
            adapter,
            ResourcesCompat.getDrawable(resources, R.drawable.baseline_done_white, null),
            ResourcesCompat.getDrawable(resources, R.drawable.baseline_delete_white, null)
        )
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.recycler)
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

enum class Visibility(val resId: Int) {
    VISIBLE(R.drawable.baseline_visibility_24),
    INVISIBLE(R.drawable.baseline_visibility_off_24)
}
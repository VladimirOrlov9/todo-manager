package com.example.todo_manager.ui.fragment.mainscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_manager.domain.model.TodoItem
import com.example.domain.usecase.LoadTodoTasksUseCase
import com.example.domain.usecase.UpdateTodoStatusUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val loadTodoTasksUseCase: LoadTodoTasksUseCase,
    private val updateTodoStatusUseCase: UpdateTodoStatusUseCase
): ViewModel() {

    private val _todoList = MutableLiveData<List<TodoItem>>()
    val todoList: LiveData<List<TodoItem>> = _todoList

    private var isFiltered: Boolean = false

    fun invIsFiltered() {
        isFiltered = !isFiltered
    }

    fun getIsFiltered(): Boolean = isFiltered

    fun loadTodoList() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = loadTodoTasksUseCase.execute()
            _todoList.postValue(list)
        }
    }

    fun updateTodoStatus(id: String, status: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            updateTodoStatusUseCase.execute(id, status)
        }
    }
}
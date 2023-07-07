package com.example.todo_manager.ui.fragment.mainscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_manager.domain.model.TodoItem
import com.example.todo_manager.domain.usecase.LoadTodoTasksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val loadTodoTasksUseCase: LoadTodoTasksUseCase
): ViewModel() {

    private val _todoList = MutableLiveData<List<TodoItem>>()
    val todoList: LiveData<List<TodoItem>> = _todoList

    fun loadTodoList() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = loadTodoTasksUseCase.execute()
            _todoList.postValue(list)
        }
    }
}
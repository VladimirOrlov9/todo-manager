package com.example.todo_manager.ui.fragment.todoscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_manager.domain.model.TodoItem
import com.example.todo_manager.domain.usecase.CreateTodoUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoScreenViewModel(
    private val createTodoUseCase: CreateTodoUseCase
): ViewModel() {
    fun createNewTodoItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            createTodoUseCase.execute(todoItem)
        }
    }

}
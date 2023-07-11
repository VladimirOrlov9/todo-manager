package com.example.todo_manager.ui.fragment.todoscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_manager.domain.model.TodoItem
import com.example.todo_manager.domain.usecase.CreateTodoUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoScreenViewModel(
    private val createTodoUseCase: CreateTodoUseCase
): ViewModel() {

    private val _dateInMillis = MutableLiveData(System.currentTimeMillis())
    val dateInMillis: LiveData<Long> = _dateInMillis

    fun setNewDate(newDate: Long) {
        _dateInMillis.value = newDate
    }

    fun createNewTodoItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            createTodoUseCase.execute(todoItem)
        }
    }

}
package com.example.todo_manager.ui.fragment.todoscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_manager.domain.model.Importance
import com.example.todo_manager.domain.model.TodoItem
import com.example.domain.usecase.CreateOrUpdateTodoUseCase
import com.example.domain.usecase.DeleteTodoUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class TodoScreenViewModel(
    private val createOrUpdateTodoUseCase: CreateOrUpdateTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase
) : ViewModel() {

    private val _dateInMillis = MutableLiveData(System.currentTimeMillis())
    val dateInMillis: LiveData<Long> = _dateInMillis

    private val _successDBOperation = MutableLiveData<Boolean>()
    val successDBOperation: LiveData<Boolean> = _successDBOperation

    private var todo: TodoItem? = null

    fun setTodo(todoItem: TodoItem) {
        todo = todoItem
    }

    fun getTodo(): TodoItem? = todo

    fun setNewDate(newDate: Long) {
        _dateInMillis.value = newDate
    }

    fun createNewTodoItem(
        description: String,
        importance: Importance,
        deadline: Long?
    ) {
        val currentDate = System.currentTimeMillis()

        val todoToSave = todo?.let { todoItem ->
            todoItem.description = description
            todoItem.importance = importance
            todoItem.deadline = deadline
            todoItem.lastEditDate = currentDate

            return@let todoItem
        } ?: kotlin.run {
            val id = UUID.randomUUID().toString()

            return@run TodoItem(
                id = id,
                description = description,
                importance = importance,
                deadline = deadline,
                creationDate = currentDate
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            val result = createOrUpdateTodoUseCase.execute(todoToSave)
            _successDBOperation.postValue(result)
        }
    }

    fun deleteTodo() {
        val todoId = todo?.id
        if (todoId != null)
            viewModelScope.launch(Dispatchers.IO) {
                val result = deleteTodoUseCase.execute(todoId)
                _successDBOperation.postValue(result)
            }
    }

}
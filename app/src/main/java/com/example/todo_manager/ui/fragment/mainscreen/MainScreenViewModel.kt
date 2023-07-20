package com.example.todo_manager.ui.fragment.mainscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.DeleteTodoUseCase
import com.example.todo_manager.domain.model.TodoItem
import com.example.domain.usecase.LoadTodoTasksUseCase
import com.example.domain.usecase.UpdateTodoStatusUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val loadTodoTasksUseCase: LoadTodoTasksUseCase,
    private val updateTodoStatusUseCase: UpdateTodoStatusUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase
) : ViewModel() {

    private val _todoList = MutableLiveData<List<TodoItem>>()
    val todoList: LiveData<List<TodoItem>> = _todoList

    private val _completed = MutableLiveData<Int>()
    val completed: LiveData<Int> = _completed

    private var originalList: MutableList<TodoItem> = mutableListOf()


    private val _isFiltered = MutableLiveData<Boolean>(false)
    val isFiltered: LiveData<Boolean> = _isFiltered

    private fun filteredList(_list: List<TodoItem>): List<TodoItem> {
        return if (isFiltered.value == true)
            _list.filter { !it.isDone }
        else
            _list
    }

    fun invIsFiltered() {
        _isFiltered.value = isFiltered.value?.not()
        _todoList.postValue(filteredList(originalList))
    }

//    fun getIsFiltered(): Boolean = isFiltered

    fun loadTodoList() {
        viewModelScope.launch(Dispatchers.IO) {
            originalList = loadTodoTasksUseCase.execute().toMutableList()

            _todoList.postValue(filteredList(originalList))
            _completed.postValue(originalList.count { it.isDone })
        }
    }

    fun updateTodoStatus(id: String, status: Boolean) {
        originalList.findLast { it.id == id }?.isDone = status
        _todoList.postValue(filteredList(originalList))

        viewModelScope.launch(Dispatchers.IO) {
            updateTodoStatusUseCase.execute(id, status)
            if (status)
                _completed.postValue(_completed.value?.inc())
            else
                _completed.postValue(_completed.value?.dec())
        }
    }

    fun deleteTodo(id: String) {
        originalList.removeIf { it.id == id }
        _todoList.postValue(filteredList(originalList))

        viewModelScope.launch(Dispatchers.IO) {
            deleteTodoUseCase.execute(id)
        }
    }
}
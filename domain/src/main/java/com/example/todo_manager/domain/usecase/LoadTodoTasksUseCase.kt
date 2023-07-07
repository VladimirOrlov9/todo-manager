package com.example.todo_manager.domain.usecase

import com.example.todo_manager.domain.model.TodoItem
import com.example.todo_manager.domain.repository.TodoItemsRepository

class LoadTodoTasksUseCase(
    private val todoItemsRepository: TodoItemsRepository
) {
    fun execute(): List<TodoItem> {
        return todoItemsRepository.getTodoItems()
    }
}
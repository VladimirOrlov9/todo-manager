package com.example.domain.usecase

import com.example.todo_manager.domain.model.TodoItem
import com.example.domain.repository.TodoItemsRepository

class LoadTodoTasksUseCase(
    private val todoItemsRepository: TodoItemsRepository
) {
    suspend fun execute(): List<TodoItem> {
        return todoItemsRepository.getTodoItems()
    }
}
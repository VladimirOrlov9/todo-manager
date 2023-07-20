package com.example.domain.usecase

import com.example.todo_manager.domain.model.TodoItem
import com.example.domain.repository.TodoItemsRepository

class CreateOrUpdateTodoUseCase(
    private val todoItemsRepository: TodoItemsRepository
) {
    suspend fun execute(todo: TodoItem): Boolean {
        return todoItemsRepository.insertOrUpdateTodo(todo)
    }
}
package com.example.todo_manager.domain.usecase

import com.example.todo_manager.domain.repository.TodoItemsRepository

class UpdateTodoStatusUseCase(
    private val todoItemsRepository: TodoItemsRepository
) {

    suspend fun execute(id: String, status: Boolean) {
        todoItemsRepository.updateTodoStatus(
            id, status
        )
    }
}
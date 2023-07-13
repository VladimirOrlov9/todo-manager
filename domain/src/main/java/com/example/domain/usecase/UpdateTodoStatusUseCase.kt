package com.example.domain.usecase

import com.example.domain.repository.TodoItemsRepository

class UpdateTodoStatusUseCase(
    private val todoItemsRepository: TodoItemsRepository
) {

    suspend fun execute(id: String, status: Boolean) {
        todoItemsRepository.updateTodoStatus(
            id, status
        )
    }
}
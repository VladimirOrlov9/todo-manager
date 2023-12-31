package com.example.domain.usecase

import com.example.domain.repository.TodoItemsRepository

class DeleteTodoUseCase(
    private val todoItemsRepository: TodoItemsRepository
) {
    suspend fun execute(todoId: String): Boolean {
        return todoItemsRepository.deleteTodoById(todoId)
    }
}
package com.example.todo_manager.domain.usecase

import com.example.todo_manager.domain.model.TodoItem
import com.example.todo_manager.domain.repository.TodoItemsRepository

class CreateTodoUseCase(
    private val todoItemsRepository: TodoItemsRepository
) {
    suspend fun execute(todo: TodoItem) {
        return todoItemsRepository.addNewTodo(todo)
    }
}
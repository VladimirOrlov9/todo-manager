package com.example.domain.repository

import com.example.todo_manager.domain.model.TodoItem

interface TodoItemsRepository {
    suspend fun getTodoItems(): List<TodoItem>
    suspend fun insertOrUpdateTodo(todoItem: TodoItem)
    suspend fun updateTodoStatus(id: String, status: Boolean)
    suspend fun deleteTodoById(todoId: String)
}
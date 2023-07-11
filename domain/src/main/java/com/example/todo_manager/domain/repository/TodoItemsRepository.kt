package com.example.todo_manager.domain.repository

import com.example.todo_manager.domain.model.TodoItem

interface TodoItemsRepository {
    suspend fun getTodoItems(): List<TodoItem>
    suspend fun addNewTodo(todoItem: TodoItem)
    suspend fun updateTodoStatus(id: String, status: Boolean)
}
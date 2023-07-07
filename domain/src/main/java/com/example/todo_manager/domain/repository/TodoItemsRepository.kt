package com.example.todo_manager.domain.repository

import com.example.todo_manager.domain.model.TodoItem

interface TodoItemsRepository {
    fun getTodoItems(): List<TodoItem>
}
package com.example.todo_manager.data.repository

import com.example.todo_manager.data.datagen.TodoDataGenerator
import com.example.todo_manager.domain.model.TodoItem
import com.example.todo_manager.domain.repository.TodoItemsRepository

class TodoItemsRepositoryImpl: TodoItemsRepository {
    override fun getTodoItems(): List<TodoItem> {
        val dataGenerator = TodoDataGenerator(System.currentTimeMillis())
        return dataGenerator.generateTodoItems(100)
    }
}
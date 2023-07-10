package com.example.todo_manager.data.storage.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todo_manager.data.storage.room.entity.TodoItemEntity
import com.example.todo_manager.domain.model.Importance
import com.example.todo_manager.domain.model.TodoItem
import com.example.todo_manager.domain.repository.TodoItemsRepository

@Dao
interface TodoDao: TodoItemsRepository {
    @Insert
    suspend fun insertTodoItem(todoItem: TodoItemEntity)

    @Query("DELETE FROM todos WHERE id is :id")
    suspend fun deleteTodoItem(id: String)

    @Update
    suspend fun updateTodoItem(todoItem: TodoItemEntity)

    @Query("SELECT * FROM todos")
    suspend fun getAllTodos(): List<TodoItemEntity>

    override suspend fun getTodoItems(): List<TodoItem> {
        val list = getAllTodos()
        println(list.toString())
        return list.mapToTodoItem()
    }
}

private fun List<TodoItemEntity>.mapToTodoItem(): List<TodoItem> {
    return this.map {
        TodoItem(
            id = it.id,
            description = it.description,
            importance = Importance.valueOf(it.importance),
            deadline = it.deadline,
            isDone = it.isDone,
            creationDate = it.creationDate,
            lastEditDate = it.lastEditDate
        )
    }
}

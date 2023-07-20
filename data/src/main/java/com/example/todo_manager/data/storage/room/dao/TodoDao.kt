package com.example.todo_manager.data.storage.room.dao

import androidx.room.*
import com.example.todo_manager.data.storage.room.entity.TodoItemEntity
import com.example.todo_manager.domain.model.Importance
import com.example.todo_manager.domain.model.TodoItem
import com.example.domain.repository.TodoItemsRepository

@Dao
interface TodoDao: TodoItemsRepository {
    @Insert
    suspend fun insertTodoItem(todoItem: TodoItemEntity): Long

    @Transaction
    override suspend fun deleteTodoById(todoId: String): Boolean {
        val deletedItems = delete(todoId)
        return deletedItems > 0
    }

    @Query("DELETE FROM todos WHERE id is :todoId")
    suspend fun delete(todoId: String): Int

    @Update
    suspend fun updateTodoItem(todoItem: TodoItemEntity): Int

    @Query("SELECT * FROM todos")
    suspend fun getAllTodos(): List<TodoItemEntity>

    @Query("UPDATE todos SET is_done = :status WHERE id = :id")
    override suspend fun updateTodoStatus(id: String, status: Boolean)

    @Query("SELECT EXISTS(SELECT * FROM todos WHERE id = :todoId)")
    suspend fun isTodoExists(todoId: String): Boolean

    @Transaction
    override suspend fun getTodoItems(): List<TodoItem> {
        val list = getAllTodos()
        println(list.toString())
        return list.mapToTodoItem()
    }

    @Transaction
    override suspend fun insertOrUpdateTodo(todoItem: TodoItem): Boolean {
        val todoEntity: TodoItemEntity = todoItem.mapToTodoItemEntity()

        return if (isTodoExists(todoEntity.id))
            updateTodoItem(todoEntity) > 0
        else
            insertTodoItem(todoEntity) > -1L
    }
}

private fun TodoItem.mapToTodoItemEntity(): TodoItemEntity =
    TodoItemEntity(
        id = this.id,
        description = this.description,
        importance = this.importance.ordinal,
        deadline = this.deadline,
        isDone = this.isDone,
        creationDate = this.creationDate,
        lastEditDate = this.lastEditDate
    )

private fun List<TodoItemEntity>.mapToTodoItem(): List<TodoItem> {
    return this.map {
        TodoItem(
            id = it.id,
            description = it.description,
            importance = Importance.values()[it.importance],
            deadline = it.deadline,
            isDone = it.isDone,
            creationDate = it.creationDate,
            lastEditDate = it.lastEditDate
        )
    }
}

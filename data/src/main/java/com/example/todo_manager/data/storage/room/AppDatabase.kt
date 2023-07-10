package com.example.todo_manager.data.storage.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todo_manager.data.storage.room.dao.TodoDao
import com.example.todo_manager.data.storage.room.entity.TodoItemEntity

@Database(entities = [TodoItemEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao
}
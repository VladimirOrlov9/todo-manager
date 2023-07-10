package com.example.todo_manager.di

import androidx.room.Room
import com.example.todo_manager.data.storage.room.AppDatabase
import com.example.todo_manager.domain.repository.TodoItemsRepository
import org.koin.dsl.module

val dataModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "todo_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    single<TodoItemsRepository> {
        val db = get<AppDatabase>()
        db.todoDao()
    }
}
package com.example.todo_manager.di

import com.example.todo_manager.data.repository.TodoItemsRepositoryImpl
import com.example.todo_manager.domain.repository.TodoItemsRepository
import org.koin.dsl.module

val dataModule = module {
    single<TodoItemsRepository> {
        TodoItemsRepositoryImpl()
    }
}
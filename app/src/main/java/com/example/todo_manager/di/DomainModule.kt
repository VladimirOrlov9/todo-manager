package com.example.todo_manager.di

import com.example.todo_manager.domain.usecase.CreateTodoUseCase
import com.example.todo_manager.domain.usecase.LoadTodoTasksUseCase
import org.koin.dsl.module

val domainModule = module {
    factory {
        LoadTodoTasksUseCase(
            todoItemsRepository = get()
        )
    }
    factory {
        CreateTodoUseCase(
            todoItemsRepository = get()
        )
    }
}
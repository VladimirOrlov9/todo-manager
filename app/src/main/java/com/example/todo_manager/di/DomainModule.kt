package com.example.todo_manager.di

import com.example.domain.usecase.CreateOrUpdateTodoUseCase
import com.example.domain.usecase.DeleteTodoUseCase
import com.example.domain.usecase.LoadTodoTasksUseCase
import com.example.domain.usecase.UpdateTodoStatusUseCase
import org.koin.dsl.module

val domainModule = module {
    factory {
        LoadTodoTasksUseCase(
            todoItemsRepository = get()
        )
    }
    factory {
        CreateOrUpdateTodoUseCase(
            todoItemsRepository = get()
        )
    }
    factory {
        UpdateTodoStatusUseCase(
            todoItemsRepository = get()
        )
    }
    factory {
        DeleteTodoUseCase(
            todoItemsRepository = get()
        )
    }
}
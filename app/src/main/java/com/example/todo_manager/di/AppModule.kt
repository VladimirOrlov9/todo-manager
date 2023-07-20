package com.example.todo_manager.di

import com.example.todo_manager.ui.fragment.mainscreen.MainScreenViewModel
import com.example.todo_manager.ui.fragment.todoscreen.TodoScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        MainScreenViewModel(
            loadTodoTasksUseCase = get(),
            updateTodoStatusUseCase = get(),
            deleteTodoUseCase = get()
        )
    }

    viewModel {
        TodoScreenViewModel(
            createOrUpdateTodoUseCase = get(),
            deleteTodoUseCase = get()
        )
    }
}
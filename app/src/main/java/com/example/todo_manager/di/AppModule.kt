package com.example.todo_manager.di

import com.example.todo_manager.ui.fragment.mainscreen.MainScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        MainScreenViewModel(
            loadTodoTasksUseCase = get()
        )
    }
}
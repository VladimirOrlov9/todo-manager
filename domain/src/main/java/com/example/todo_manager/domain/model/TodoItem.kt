package com.example.todo_manager.domain.model

data class TodoItem(
    val id: String,
    var description: String,
    var importance: Importance,
    val deadline: Long? = null,
    var isDone: Boolean,
    val creationDate: Long,
    var lastEditDate: Long? = null
)

enum class Importance {
    LOW,
    MEDIUM,
    IMMEDIATE
}
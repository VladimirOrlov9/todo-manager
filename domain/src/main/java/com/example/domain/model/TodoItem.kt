package com.example.todo_manager.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TodoItem(
    val id: String,
    var description: String,
    var importance: Importance,
    var deadline: Long? = null,
    var isDone: Boolean = false,
    val creationDate: Long,
    var lastEditDate: Long? = null
): Parcelable

enum class Importance {
    LOW,
    MEDIUM,
    IMMEDIATE
}
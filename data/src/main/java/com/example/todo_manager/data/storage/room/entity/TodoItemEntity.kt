package com.example.todo_manager.data.storage.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class TodoItemEntity(
    @PrimaryKey val id: String,
    var description: String,
    var importance: String,
    val deadline: Long? = null,
    @ColumnInfo(name = "is_done") var isDone: Boolean,
    @ColumnInfo(name = "creation_date") val creationDate: Long,
    @ColumnInfo(name = "last_edit_date") var lastEditDate: Long? = null
)

package com.example.todo_manager.data.datagen

import com.example.todo_manager.domain.model.Importance
import com.example.todo_manager.domain.model.TodoItem
import kotlin.random.Random
import kotlin.random.nextLong

class TodoDataGenerator(
    private val currentTimeInMillis: Long
) {
    private val random = Random(currentTimeInMillis)

    private fun generateItem(id: String): TodoItem {
        val description = descriptionVariations.random()
        val importance = Importance.values().random()
        val creationDate = currentTimeInMillis - random.nextLong(10000L..100000000)

        return TodoItem(
            id = id,
            description = description,
            importance = importance,
            isDone = random.nextBoolean(),
            creationDate = creationDate
        )
    }

    fun generateTodoItems(count: Int): List<TodoItem> {
        val list = mutableListOf<TodoItem>()
        for (i in 0..count) {
            list.add(generateItem(id = "$i"))
        }
        return list
    }
}
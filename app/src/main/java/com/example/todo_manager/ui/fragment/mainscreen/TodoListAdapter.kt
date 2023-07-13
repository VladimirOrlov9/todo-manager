package com.example.todo_manager.ui.fragment.mainscreen

import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_manager.R
import com.example.todo_manager.domain.model.TodoItem

const val VIEW_TYPE_TODO = 1
const val VIEW_TYPE_NEW_TODO = 2

class TodoListAdapter(
    private val todoInfoClickEvent: (TodoItem) -> Unit,
    private val todoCheckBoxStatusChangedEvent: (String, Boolean) -> Unit,
    private val newTodoClickEvent: () -> Unit
) : ListAdapter<TodoItem, RecyclerView.ViewHolder>(DiffCallback()) {

    private var hideCompletedFlag: Boolean = false
    private var originalList = listOf<TodoItem>()

    fun submitListWithFilterApply(list: List<TodoItem>) {
        originalList = list

        submitList(filteredList())
    }

    fun changeVisibility() {
        hideCompletedFlag = !hideCompletedFlag
        submitListWithFilterApply(originalList)
    }

    private fun filteredList(): List<TodoItem> {
        return if (hideCompletedFlag)
            originalList.filter { !it.isDone }
        else
            originalList
    }

    fun checkVisibility(): Boolean = hideCompletedFlag

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val isDoneCheckBox = itemView.findViewById<AppCompatCheckBox>(R.id.is_done)
        private val descriptionTextView =
            itemView.findViewById<AppCompatTextView>(R.id.todo_description)
        private val infoButton = itemView.findViewById<AppCompatImageButton>(R.id.todo_info_button)

        private fun setDescriptionText(description: String, isChecked: Boolean) {
            descriptionTextView.apply {
                when (isChecked) {
                    true -> {
                        text = SpannableString(description).apply {
                            setSpan(StrikethroughSpan(), 0, description.length, 0)
                        }
                        setTextColor(
                            ResourcesCompat.getColor(
                                itemView.resources,
                                R.color.label_tertiary,
                                null
                            )
                        )
                    }
                    else -> {
                        text = description
                        setTextColor(
                            ResourcesCompat.getColor(
                                itemView.resources,
                                R.color.label_primary,
                                null
                            )
                        )
                    }
                }
            }
        }

        fun bind(position: Int) {
            val currentItem = currentList[position]

            isDoneCheckBox.isChecked = currentItem.isDone
            setDescriptionText(currentItem.description, currentItem.isDone)

            infoButton.setOnClickListener {
                todoInfoClickEvent(currentItem)
            }

            isDoneCheckBox.setOnCheckedChangeListener { _, isChecked ->
                setDescriptionText(currentItem.description, isChecked)

                originalList.findLast { it.id == currentItem.id }?.apply {
                    isDone = !isDone
                }
                submitListWithFilterApply(originalList)

                todoCheckBoxStatusChangedEvent(currentItem.id, isChecked)
            }
        }
    }

    inner class NewTodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.setOnClickListener {
                newTodoClickEvent()
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<TodoItem>() {
        override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
            oldItem == newItem

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == currentList.size)
            VIEW_TYPE_NEW_TODO
        else
            VIEW_TYPE_TODO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_TODO -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.todo_list_item, parent, false)
                TodoViewHolder(itemView)
            }
            else -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.add_todo_item, parent, false)
                NewTodoViewHolder(itemView)
            }
        }
    }

    override fun getItemCount(): Int = currentList.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == currentList.size && holder is NewTodoViewHolder)
            holder.bind()
        else if (holder is TodoViewHolder)
            holder.bind(position)
    }
}
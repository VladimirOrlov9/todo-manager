package com.example.todo_manager.ui.fragment.mainscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_manager.R
import com.example.todo_manager.domain.model.TodoItem

class TodoListAdapter(
    private val onClickEvent: (String) -> Unit
): ListAdapter<TodoItem, TodoListAdapter.TodoViewHolder>(DiffCallback()) {

    inner class TodoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val isDoneCheckBox = itemView.findViewById<AppCompatCheckBox>(R.id.is_done)
        private val descriptionTextView = itemView.findViewById<AppCompatTextView>(R.id.todo_description)
        private val infoButton = itemView.findViewById<AppCompatImageButton>(R.id.todo_info_button)

        fun bind(position: Int) {
            val currentItem = currentList[position]

            isDoneCheckBox.isChecked = currentItem.isDone
            descriptionTextView.text = currentItem.description
            infoButton.setOnClickListener {
                onClickEvent(currentItem.id)
            }
        }
    }

    class DiffCallback:DiffUtil.ItemCallback<TodoItem>() {
        override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
            oldItem == newItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_list_item, parent, false)
        return TodoViewHolder(itemView)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(position)
    }
}
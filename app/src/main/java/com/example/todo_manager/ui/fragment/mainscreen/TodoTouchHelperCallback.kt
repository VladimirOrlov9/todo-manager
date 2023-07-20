package com.example.todo_manager.ui.fragment.mainscreen

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class TodoTouchHelperCallback(
    private val swipeActions: SwipeActions,
    private val checkSwipeIcon: Drawable?,
    private val deleteSwipeIcon: Drawable?
) : ItemTouchHelper.Callback() {

    private val paint = Paint()

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if (viewHolder is TodoListAdapter.NewTodoViewHolder)
            return 0
        val dragFlags = 0
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (viewHolder is TodoListAdapter.TodoViewHolder) {
            when (direction) {
                ItemTouchHelper.START -> swipeActions.onItemDeleted(viewHolder.adapterPosition)
                ItemTouchHelper.END -> swipeActions.onItemChecked(viewHolder.adapterPosition)
            }
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView
            if (dX > 0) { // swipe to the right
                paint.color = Color.GREEN
                c.drawRect(
                    itemView.left.toFloat(),
                    itemView.top.toFloat(),
                    itemView.left + dX,
                    itemView.bottom.toFloat(),
                    paint
                )
                if (checkSwipeIcon != null) {
                    drawLeftIcon(c, checkSwipeIcon, itemView, dX)
                }

                itemView.alpha = itemView.width + abs(dX)
                itemView.translationX = dX
            }
            if (dX < 0) { // swipe to the left
                paint.color = Color.RED
                c.drawRect(
                    itemView.right - abs(dX),
                    itemView.top.toFloat(),
                    itemView.right.toFloat(),
                    itemView.bottom.toFloat(),
                    paint
                )
                if (deleteSwipeIcon != null) {
                    drawRightIcon(c, deleteSwipeIcon, itemView, dX)
                }

                itemView.alpha = itemView.width - abs(dX)
                itemView.translationX = dX
            }
        }
        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    private fun drawLeftIcon(c: Canvas, icon: Drawable, itemView: View, dX: Float) {
        val checkIconMargin = (itemView.height - icon.intrinsicHeight) / 2

        val checkIconTop = itemView.top + checkIconMargin
        val checkIconBottom = itemView.bottom - checkIconMargin
        val checkIconLeft = if (dX > icon.intrinsicWidth)
            (itemView.left + dX) / 2 - icon.intrinsicWidth / 2
        else
            itemView.left - (icon.intrinsicWidth - abs(dX))
        val checkIconRight = if (dX > icon.intrinsicWidth)
            (itemView.left + dX) / 2 + icon.intrinsicWidth / 2
        else
            itemView.left + abs(dX)

        icon.setBounds(checkIconLeft.toInt(), checkIconTop, checkIconRight.toInt(), checkIconBottom)

        icon.draw(c)
    }

    private fun drawRightIcon(c: Canvas, icon: Drawable, itemView: View, dX: Float) {
        val iconMargin = (itemView.height - icon.intrinsicHeight) / 2

        val left = itemView.right - abs(dX)
        val checkIconTop = itemView.top + iconMargin
        val checkIconBottom = itemView.bottom - iconMargin
        val checkIconLeft = if (abs(dX) > icon.intrinsicWidth)
            left + (itemView.right - left) / 2 - icon.intrinsicWidth / 2
        else
            itemView.right - abs(dX)
        val checkIconRight = if (abs(dX) > icon.intrinsicWidth)
            left + (itemView.right - left) / 2 + icon.intrinsicWidth / 2
        else
            itemView.right - abs(dX) + icon.intrinsicWidth

        icon.setBounds(checkIconLeft.toInt(), checkIconTop, checkIconRight.toInt(), checkIconBottom)
        icon.draw(c)
    }

}

interface SwipeActions {
    fun onItemDeleted(position: Int)
    fun onItemChecked(position: Int)
}
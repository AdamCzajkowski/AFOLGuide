package com.application.afol.utility

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.application.afol.R
import com.application.afol.ui.adapters.LegoRecyclerViewAdapter


class DragManageAddAdapter(adapter: LegoRecyclerViewAdapter, val context: Context) :
    ItemTouchHelper.Callback() {
    private var nameAdapter = adapter

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        nameAdapter.swapItem(viewHolder.adapterPosition)
    }


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int = makeMovementFlags(0, ItemTouchHelper.RIGHT)


    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        val background = ColorDrawable(context.getColor(R.color.green))
        val icon = ContextCompat.getDrawable(context, R.drawable.ic_library_add_white_36dp)
        val itemView = viewHolder.itemView
        val iconTop = itemView.top + (0.35 * itemView.height).toInt()
        val iconBottom = itemView.bottom - (0.35 * itemView.height).toInt()
        val iconLeft = itemView.left + (0.1 * itemView.width).toInt()
        val iconRight = itemView.right - (0.78 * itemView.width).toInt()

        icon!!.setBounds(iconLeft, iconTop, iconRight, iconBottom)

        background.setBounds(
            itemView.left,
            itemView.top,
            itemView.right,
            itemView.bottom
        )

        background.draw(c)
        icon.draw(c)
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}
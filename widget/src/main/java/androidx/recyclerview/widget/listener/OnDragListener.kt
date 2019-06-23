package androidx.recyclerview.widget.listener

import androidx.recyclerview.widget.RecyclerView

interface OnDragListener {

    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    fun onDragStart(viewHolder: RecyclerView.ViewHolder)
}

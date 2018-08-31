package soup.widget.recyclerview.listener

import androidx.recyclerview.widget.RecyclerView

/**
 * Called when a view is requesting a start of a drag.
 *
 * @param viewHolder The holder of the view to drag.
 */
typealias OnStartDragListener = (RecyclerView.ViewHolder) -> Unit

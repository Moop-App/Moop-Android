package soup.movie.theater.sort

import android.view.MotionEvent
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.listener.OnDragListener
import androidx.recyclerview.widget.listener.OnItemMoveListener
import soup.movie.databinding.DataBindingAdapter
import soup.movie.databinding.DataBindingViewHolder
import soup.movie.ext.swap
import soup.movie.model.Theater
import soup.movie.theater.R

internal class TheaterSortListAdapter: DataBindingAdapter<Theater>(), OnItemMoveListener {

    private var dragListener: OnDragListener? = null

    fun setOnDragListener(listener: OnDragListener) {
        this.dragListener = listener
    }

    override fun createViewHolder(binding: ViewDataBinding): DataBindingViewHolder<Theater> {
        return super.createViewHolder(binding).apply {
            itemView.findViewById<View>(R.id.dragHandle)
                .setOnTouchListener { _, event ->
                    if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                        dragListener?.onDragStart(this)
                    }
                    false
                }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)?.type) {
            Theater.TYPE_CGV -> R.layout.theater_sort_item_cgv
            Theater.TYPE_LOTTE -> R.layout.theater_sort_item_lotte
            Theater.TYPE_MEGABOX -> R.layout.theater_sort_item_megabox
            else -> throw IllegalArgumentException("This is not valid type.")
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        items.swap(fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }
}

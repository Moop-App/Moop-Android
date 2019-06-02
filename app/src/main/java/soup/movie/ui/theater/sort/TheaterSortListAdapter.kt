package soup.movie.ui.theater.sort

import android.view.MotionEvent
import androidx.recyclerview.widget.listener.OnDragStartListener
import androidx.recyclerview.widget.listener.OnItemMoveListener
import kotlinx.android.synthetic.main.theater_sort_item_cgv.view.*
import soup.movie.R
import soup.movie.data.model.Theater
import soup.movie.ui.databinding.DataBindingAdapter
import soup.movie.ui.databinding.DataBindingViewHolder
import soup.movie.util.swap

internal class TheaterSortListAdapter(
    private val dragStartListener: OnDragStartListener
) : DataBindingAdapter<Theater>(), OnItemMoveListener {

    override fun onBindViewHolder(holder: DataBindingViewHolder<Theater>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.dragHandle.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                dragStartListener.onDragStart(holder)
            }
            false
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position).type) {
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

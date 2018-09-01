package soup.movie.ui.theater.sort

import android.view.MotionEvent
import kotlinx.android.synthetic.main.item_theater.view.*
import soup.movie.R
import soup.movie.data.model.Theater
import soup.movie.ui.helper.databinding.DataBindingAdapter
import soup.movie.ui.helper.databinding.DataBindingViewHolder
import soup.widget.recyclerview.listener.OnItemMoveListener
import soup.widget.recyclerview.listener.OnDragStartListener
import java.util.*

internal class TheaterSortListAdapter(private val dragStartListener: OnDragStartListener) :
        DataBindingAdapter<Theater>(), OnItemMoveListener {

    override fun onBindViewHolder(holder: DataBindingViewHolder<Theater>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.dragHandle.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                dragStartListener.onDragStart(holder)
            }
            false
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_theater

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(list, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }
}

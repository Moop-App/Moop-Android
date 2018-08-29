package soup.movie.ui.theater.sort

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_theater.view.*
import soup.movie.R
import soup.movie.data.model.Theater
import soup.movie.util.inflate
import soup.widget.drag.ItemTouchHelperAdapter
import soup.widget.drag.OnStartDragListener
import java.util.*

internal class TheaterSortListAdapter(
        private val _selectedItems: List<Theater>,
        private val dragStartListener: OnStartDragListener)
    : RecyclerView.Adapter<TheaterSortListAdapter.ViewHolder>(), ItemTouchHelperAdapter {

    val selectedTheaters: List<Theater>
        get() = _selectedItems.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_theater))

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val theaterItem = selectedTheaters[position]
        holder.bindItem(theaterItem)
        holder.itemView.drag_handle.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                dragStartListener.onStartDrag(holder)
            }
            false
        }
    }

    override fun getItemCount(): Int {
        return selectedTheaters.size
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(selectedTheaters, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {}

    internal class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItem(data: Theater) {
            itemView.chip_theater.let {
                it.chipText = data.name
                it.transitionName = data.code
                it.tag = data.code
            }
        }
    }
}

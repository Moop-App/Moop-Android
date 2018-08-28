package soup.movie.ui.theater.sort

import android.annotation.SuppressLint
import android.support.design.chip.Chip
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import soup.movie.R
import soup.movie.data.model.Theater
import soup.widget.drag.ItemTouchHelperAdapter
import soup.widget.drag.OnStartDragListener
import java.util.*

internal class TheaterSortListAdapter(
        private val _selectedItems: List<Theater>,
        private val dragStartListener: OnStartDragListener)
    : RecyclerView.Adapter<TheaterSortListAdapter.ViewHolder>(), ItemTouchHelperAdapter {

    val selectedTheaters: List<Theater>
        get() = _selectedItems.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_theater, parent, false))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val theaterItem = selectedTheaters[position]
        holder.bindType(theaterItem)

        holder.dragHandle.setOnTouchListener { _, event ->
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

        @BindView(R.id.chip_theater)
        lateinit var theaterChip: Chip

        @BindView(R.id.drag_handle)
        lateinit var dragHandle: View

        init {
            ButterKnife.bind(this, view)
        }

        fun bindType(data: Theater) {
            theaterChip.chipText = data.name
            theaterChip.transitionName = data.code
            theaterChip.tag = data.code
        }
    }
}

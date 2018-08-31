package soup.movie.ui.theater.sort

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_theater.view.*
import soup.movie.R
import soup.movie.data.model.Theater
import soup.movie.util.inflate
import soup.widget.recyclerview.util.ItemTouchHelperAdapter
import soup.widget.recyclerview.listener.OnStartDragListener
import java.util.*

internal class TheaterSortListAdapter(_theaters: List<Theater>,
                                      private val dragStartListener: OnStartDragListener)
    : RecyclerView.Adapter<TheaterSortListAdapter.ViewHolder>(), ItemTouchHelperAdapter {

    val theaters: MutableList<Theater> = _theaters.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent)

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val theaterItem = theaters[position]
        holder.bindItem(theaterItem)
        holder.itemView.dragHandle.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                dragStartListener.onStartDrag(holder)
            }
            false
        }
    }

    override fun getItemCount(): Int {
        return theaters.size
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(theaters, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {}

    internal class ViewHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(parent.inflate(R.layout.item_theater)) {

        fun bindItem(data: Theater) {
            itemView.theaterChip.apply {
                text = data.name
                transitionName = data.code
                tag = data.code
            }
        }
    }
}

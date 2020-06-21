package soup.movie.theater.sort

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.listener.OnDragListener
import androidx.recyclerview.widget.listener.OnItemMoveListener
import soup.movie.ext.swap
import soup.movie.model.Theater
import soup.movie.theater.R
import soup.movie.theater.databinding.TheaterSortItemCgvBinding
import soup.movie.theater.databinding.TheaterSortItemLotteBinding
import soup.movie.theater.databinding.TheaterSortItemMegaboxBinding

internal class TheaterSortListAdapter : RecyclerView.Adapter<TheaterSortListAdapter.ViewHolder>(), OnItemMoveListener {

    private val items = mutableListOf<Theater>()
    private var dragListener: OnDragListener? = null

    fun setOnDragListener(listener: OnDragListener) {
        this.dragListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewHolder = when (viewType) {
            R.layout.theater_sort_item_cgv ->
                CgvViewHolder(TheaterSortItemCgvBinding.inflate(layoutInflater, parent, false))
            R.layout.theater_sort_item_lotte ->
                LotteViewHolder(TheaterSortItemLotteBinding.inflate(layoutInflater, parent, false))
            R.layout.theater_sort_item_megabox ->
                MegaboxViewHolder(TheaterSortItemMegaboxBinding.inflate(layoutInflater, parent, false))
            else -> throw IllegalArgumentException("This is not valid type.")
        }
        return viewHolder.apply {
            itemView.findViewById<View>(R.id.dragHandle)
                .setOnTouchListener { _, event ->
                    if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                        dragListener?.onDragStart(this)
                    }
                    false
                }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).type) {
            Theater.TYPE_CGV -> R.layout.theater_sort_item_cgv
            Theater.TYPE_LOTTE -> R.layout.theater_sort_item_lotte
            Theater.TYPE_MEGABOX -> R.layout.theater_sort_item_megabox
            else -> throw IllegalArgumentException("This is not valid type.")
        }
    }

    override fun getItemCount(): Int = items.size

    private fun getItem(position: Int): Theater = items[position]

    fun submitList(list: List<Theater>) {
        this.items.clear()
        this.items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        items.swap(fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    internal abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: Theater)
    }

    internal class CgvViewHolder(private val binding: TheaterSortItemCgvBinding) : ViewHolder(binding.root) {

        override fun bind(item: Theater) {
            binding.theaterChip.run {
                tag = item.id
                text = item.name
                transitionName = item.id
            }
        }
    }

    internal class LotteViewHolder(private val binding: TheaterSortItemLotteBinding) : ViewHolder(binding.root) {

        override fun bind(item: Theater) {
            binding.theaterChip.run {
                tag = item.id
                text = item.name
                transitionName = item.id
            }
        }
    }

    internal class MegaboxViewHolder(private val binding: TheaterSortItemMegaboxBinding) : ViewHolder(binding.root) {

        override fun bind(item: Theater) {
            binding.theaterChip.run {
                tag = item.id
                text = item.name
                transitionName = item.id
            }
        }
    }
}

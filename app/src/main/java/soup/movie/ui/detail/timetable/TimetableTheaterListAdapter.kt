package soup.movie.ui.detail.timetable

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_timetable_theater_cgv.view.*
import soup.movie.R
import soup.movie.data.model.Theater
import soup.movie.data.model.TheaterWithTimetable
import soup.movie.data.model.Time
import soup.movie.databinding.ItemTimetableTheaterCgvBinding
import soup.movie.databinding.ItemTimetableTheaterLotteBinding
import soup.movie.databinding.ItemTimetableTheaterMegaboxBinding
import soup.movie.ui.detail.timetable.TimetableTheaterListAdapter.ViewHolder
import soup.movie.ui.helper.databinding.AbsDataBindingListAdapter
import soup.movie.ui.helper.databinding.DataBindingViewHolder
import soup.movie.util.setVisibleIf
import soup.widget.recyclerview.FixedLinearLayoutManager
import soup.widget.recyclerview.callback.IdBasedDiffCallback

internal class TimetableTheaterListAdapter(private val listener: Listener) :
        AbsDataBindingListAdapter<TheaterWithTimetable, ViewHolder, ViewDataBinding>(IdBasedDiffCallback()) {

    private val viewPool = RecyclerView.RecycledViewPool()

    interface Listener {

        fun onItemClick(item: TheaterWithTimetable)

        fun onItemClick(item: Theater)

        fun onItemClick(item: Time)
    }

    override fun createViewHolder(binding: ViewDataBinding): ViewHolder {
        return ViewHolder(binding) {
            listener.onItemClick(it)
        }.apply {
            itemView.setOnClickListener {
                listener.onItemClick(getItem(adapterPosition))
            }
            itemView.theaterView.setOnClickListener { _ ->
                listener.onItemClick(getItem(adapterPosition))
            }
            itemView.hallListView.setRecycledViewPool(viewPool)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.let {
            getItem(position)?.run {
                it.hallListView?.setVisibleIf { selected and hallList.isNotEmpty() }
                it.noResultView?.setVisibleIf { selected and hallList.isEmpty() }
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position).theater.type) {
        Theater.TYPE_CGV -> R.layout.item_timetable_theater_cgv
        Theater.TYPE_LOTTE -> R.layout.item_timetable_theater_lotte
        Theater.TYPE_MEGABOX -> R.layout.item_timetable_theater_megabox
        else -> throw IllegalArgumentException("$this is not valid type.")
    }

    class ViewHolder(binding: ViewDataBinding, listener: (Time) -> Unit) :
            DataBindingViewHolder<TheaterWithTimetable>(binding) {

        private val listAdapter = TimetableHallListAdapter(listener)

        init {
            when (binding) {
                //TODO: Please refactor this
                is ItemTimetableTheaterCgvBinding -> {
                    binding.hallListView.layoutManager = FixedLinearLayoutManager(itemView.context)
                    binding.hallListView.itemAnimator = null
                    binding.hallListView.adapter = listAdapter
                }
                is ItemTimetableTheaterLotteBinding -> {
                    binding.hallListView.layoutManager = FixedLinearLayoutManager(itemView.context)
                    binding.hallListView.itemAnimator = null
                    binding.hallListView.adapter = listAdapter
                }
                is ItemTimetableTheaterMegaboxBinding -> {
                    binding.hallListView.layoutManager = FixedLinearLayoutManager(itemView.context)
                    binding.hallListView.itemAnimator = null
                    binding.hallListView.adapter = listAdapter
                }
            }
        }

        override fun bind(item: TheaterWithTimetable) {
            super.bind(item)
            listAdapter.submitList(item.hallList)
        }
    }
}

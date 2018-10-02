package soup.movie.ui.detail.timetable

import android.view.ViewGroup
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.item_timetable_theater_cgv.view.*
import soup.movie.R
import soup.movie.data.helper.getTimetableLayout
import soup.movie.data.model.Theater
import soup.movie.data.model.TheaterWithTimetable
import soup.movie.ui.helper.databinding.DataBindingListAdapter
import soup.movie.ui.helper.databinding.DataBindingViewHolder
import soup.movie.util.inflate
import soup.movie.util.setVisibleIf
import soup.widget.recyclerview.callback.IdBasedDiffCallback

internal class TimetableTheaterListAdapter(private val listener: Listener) :
        DataBindingListAdapter<TheaterWithTimetable>(IdBasedDiffCallback()) {

    interface Listener {

        fun onItemClick(item: TheaterWithTimetable)

        fun onItemClick(item: Theater)

        fun onItemClick(item: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<TheaterWithTimetable> =
            super.onCreateViewHolder(parent, viewType).apply {
                itemView.setOnClickListener {
                    listener.onItemClick(getItem(adapterPosition))
                }
                itemView.theaterView.setOnClickListener { _ ->
                    listener.onItemClick(getItem(adapterPosition).theater)
                }
            }

    override fun onBindViewHolder(holder: DataBindingViewHolder<TheaterWithTimetable>, position: Int) {
        super.onBindViewHolder(holder, position)
        getItem(position)?.run {
            holder.itemView.timeListView?.run {
                removeAllViews()
                setVisibleIf { selected and hallList.isNotEmpty() }
                hallList.map { hall ->
                    inflate<Chip>(context, R.layout.chip_time).apply {
                        text = hall.name
                        setOnClickListener {
                            //listener.onItemClick(hall)
                        }
                    }
                }.forEach { addView(it) }
            }
            holder.itemView.noResultView?.setVisibleIf { selected and hallList.isEmpty() }
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).theater.getTimetableLayout()
}

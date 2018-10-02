package soup.movie.ui.detail.timetable

import android.view.ViewGroup
import soup.movie.R
import soup.movie.data.model.Time
import soup.movie.ui.helper.databinding.DataBindingListAdapter
import soup.movie.ui.helper.databinding.DataBindingViewHolder
import soup.widget.recyclerview.callback.AlwaysDiffCallback

internal class TimetableTimeListAdapter(val listener: (Time) -> Unit) :
        DataBindingListAdapter<Time>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<Time> {
        return super.onCreateViewHolder(parent, viewType).apply {
            itemView.setOnClickListener {
                listener(getItem(adapterPosition))
            }
        }
    }
    override fun getItemViewType(position: Int): Int = R.layout.item_timetable_time
}

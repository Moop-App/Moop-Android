package soup.movie.ui.detail.timetable

import android.view.ViewGroup
import soup.movie.R
import soup.movie.data.model.ScreeningDate
import soup.movie.ui.helper.databinding.DataBindingListAdapter
import soup.movie.ui.helper.databinding.DataBindingViewHolder
import soup.widget.recyclerview.callback.IdBasedDiffCallback

internal class TimetableDateListAdapter(val listener: (ScreeningDate) -> Unit) :
        DataBindingListAdapter<ScreeningDate>(IdBasedDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<ScreeningDate> {
        return super.onCreateViewHolder(parent, viewType).apply {
            itemView.setOnClickListener {
                listener(getItem(adapterPosition))
            }
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_timetable_date
}

package soup.movie.ui.detail.timetable

import soup.movie.R
import soup.movie.data.model.Hall
import soup.movie.databinding.ItemTimetableHallBinding
import soup.movie.ui.detail.timetable.TimetableHallListAdapter.ViewHolder
import soup.movie.ui.helper.databinding.AbsDataBindingListAdapter
import soup.movie.ui.helper.databinding.DataBindingViewHolder
import soup.widget.recyclerview.callback.AlwaysDiffCallback

internal class TimetableHallListAdapter :
        AbsDataBindingListAdapter<Hall, ViewHolder, ItemTimetableHallBinding>(AlwaysDiffCallback()) {

    override fun createViewHolder(binding: ItemTimetableHallBinding): ViewHolder = ViewHolder(binding)

    override fun getItemViewType(position: Int): Int = R.layout.item_timetable_hall

    class ViewHolder(binding: ItemTimetableHallBinding) :
            DataBindingViewHolder<Hall>(binding) {

        private val listAdapter = TimetableTimeListAdapter {
            //TODO: if needs, implements this
        }

        init {
            binding.timeListView.itemAnimator = null
            binding.timeListView.adapter = listAdapter
        }

        override fun bind(item: Hall) {
            super.bind(item)
            listAdapter.submitList(item.timeList)
        }
    }
}

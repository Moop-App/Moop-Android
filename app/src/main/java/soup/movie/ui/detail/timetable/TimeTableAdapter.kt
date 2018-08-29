package soup.movie.ui.detail.timetable

import android.content.Context
import android.support.design.chip.Chip
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_timetable_day.view.*
import soup.movie.R
import soup.movie.data.model.Day
import soup.movie.util.AlwaysDiffCallback
import soup.movie.util.executeCgvApp
import soup.movie.util.inflate

internal class TimeTableAdapter(
        private val ctx: Context)
    : ListAdapter<Day, TimeTableAdapter.DayViewHolder>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder =
            DayViewHolder(parent).also { it ->
                it.itemView.setOnClickListener { _ ->
                    //TODO: show notification with selected date and time
                    ctx.executeCgvApp()
                }
            }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        getItem(position)?.let { it ->
            with(holder.itemView) {
                this.dateView.text = it.date
                this.timeListView.removeAllViews()
                it.timeList?.forEach { time ->
                    val timeChip = View.inflate(ctx, R.layout.chip_time, null) as Chip
                    timeChip.chipText = time
                    this.timeListView.addView(timeChip)
                }
            }
        }
    }

    internal class DayViewHolder(parent: ViewGroup) :
            ViewHolder(parent.inflate(R.layout.item_timetable_day))
}

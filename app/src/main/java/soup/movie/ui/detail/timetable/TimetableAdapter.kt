package soup.movie.ui.detail.timetable

import android.content.Context

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.item_timetable.view.*
import soup.movie.R
import soup.movie.data.model.Day
import soup.movie.util.AlwaysDiffCallback
import soup.movie.util.executeCgvApp
import soup.movie.util.inflate

internal class TimetableAdapter(private val ctx: Context) :
        ListAdapter<Day, TimetableAdapter.DayViewHolder>(AlwaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder =
            DayViewHolder(parent).apply {
                itemView.setOnClickListener { _ ->
                    //TODO: show notification with selected date and time
                    ctx.executeCgvApp()
                }
            }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        getItem(position)?.run {
            holder.itemView.apply {
                dateView.text = date
                timeListView.removeAllViews()
                timeList?.forEach {
                    val timeChip = View.inflate(ctx, R.layout.chip_time, null) as Chip
                    timeChip.text = it
                    timeListView.addView(timeChip)
                }
            }
        }
    }

    internal class DayViewHolder(parent: ViewGroup) :
            ViewHolder(parent.inflate(R.layout.item_timetable))
}

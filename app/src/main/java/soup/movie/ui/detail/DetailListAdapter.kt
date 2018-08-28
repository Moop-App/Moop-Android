package soup.movie.ui.detail

import android.app.Activity
import android.content.Intent
import android.support.design.chip.Chip
import android.support.design.chip.ChipGroup
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindViews
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.item_timetable.view.*
import kotlinx.android.synthetic.main.item_timetable_none.view.*
import kotlinx.android.synthetic.main.item_trailer.view.*
import soup.movie.R
import soup.movie.data.model.TimeTable
import soup.movie.data.model.Trailer
import soup.movie.ui.theater.edit.TheaterEditActivity
import soup.movie.util.ImageUtil
import soup.movie.util.ListUtil
import soup.movie.util.MovieAppUtil
import soup.movie.util.YouTubeUtil
import java.util.*

internal class DetailListAdapter(private val host: Activity)
    : RecyclerView.Adapter<ViewHolder>() {

    private var timeTable: TimeTable = TimeTable()
    private var items: List<Trailer> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_TIMETABLE_NONE -> createTimeTableNoneHolder(parent)
            TYPE_TIMETABLE -> createTimeTableHolder(parent)
            TYPE_TRAILER -> createTrailerHolder(parent)
            else -> throw IllegalStateException("The view type($viewType) is unknown.")
        }
    }

    private fun createTimeTableNoneHolder(parent: ViewGroup): ViewHolder {
        val listener = View.OnClickListener {
            host.startActivity(Intent(host, TheaterEditActivity::class.java))
        }
        return NoneTimeTableViewHolder(parent).also {
            it.itemView.setOnClickListener(listener)
            it.itemView.select.setOnClickListener(listener)
        }
    }

    private fun createTimeTableHolder(parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_timetable, parent, false)
        return TimeTableViewHolder(view).also { it ->
            it.itemView.setOnClickListener {
                //TODO: show notification with selected date and time
                MovieAppUtil.executeCgvApp(host)
            }
        }
    }

    private fun createTrailerHolder(parent: ViewGroup): ViewHolder {
        return TrailerViewHolder(parent).also {
            it.itemView.setOnClickListener { _ ->
                YouTubeUtil.executeYoutubeApp(host, items[it.adapterPosition].youtubeId)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is TimeTableViewHolder -> onBindViewHolderInternal(holder)
            is TrailerViewHolder -> {
                val item = items[position - 1]
                ImageUtil.loadAsync(host, holder.itemView.trailerThumbnailView, item.getThumbnailUrl())
                holder.itemView.titleView.text = item.title
                holder.itemView.authorView.text = item.author
            }
        }
    }

    private fun onBindViewHolderInternal(holder: TimeTableViewHolder) {
        val item = timeTable
        val days = item.dayList
        if (!days.isEmpty()) {
            holder.itemView.empty.visibility = View.GONE
            val size = Math.min(3, days.size)
            for (i in 0 until size) {
                val (date, timeList) = days[i]
                holder.dates[i].text = date
                holder.dates[i].visibility = View.VISIBLE
                if (holder.times[i].childCount == 0) {
                    for (time in timeList) {
                        val timeChip = View.inflate(host, R.layout.chip_time, null) as Chip
                        timeChip.chipText = time
                        holder.times[i].addView(timeChip)
                    }
                }
                holder.times[i].visibility = View.VISIBLE
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position != 0) TYPE_TRAILER else if (timeTable.dayList.isEmpty()) TYPE_TIMETABLE_NONE else TYPE_TIMETABLE
    }

    override fun getItemCount(): Int {
        return ListUtil.size(items) + calcMoreCount(timeTable)
    }

    private fun calcMoreCount(timeTable: TimeTable?): Int {
        return if (timeTable != null) 1 else 0
    }

    fun updateList(timeTable: TimeTable, newItems: List<Trailer>) {
        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return ListUtil.size(items) + calcMoreCount(this@DetailListAdapter.timeTable)
            }

            override fun getNewListSize(): Int {
                return ListUtil.size(newItems) + calcMoreCount(timeTable)
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return false
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return false
            }
        }, false)
        this.timeTable = timeTable
        items = newItems
        result.dispatchUpdatesTo(this)
    }

    private class TrailerViewHolder(parent: ViewGroup) :
            ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_trailer, parent, false))

    //TODO:
    internal class TimeTableViewHolder(view: View) : ViewHolder(view) {

        @BindViews(R.id.time1, R.id.time2, R.id.time3)
        lateinit var times: Array<ChipGroup>
        @BindViews(R.id.date1, R.id.date2, R.id.date3)
        lateinit var dates: Array<TextView>

        init {
            ButterKnife.bind(this, view)
        }
    }

    private class NoneTimeTableViewHolder(parent: ViewGroup) :
            ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_timetable_none, parent, false))

    companion object {

        private const val TYPE_TIMETABLE_NONE = 0
        private const val TYPE_TIMETABLE = 1
        private const val TYPE_TRAILER = 2
    }
}

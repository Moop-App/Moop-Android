package soup.movie.ui.theater.edit

import androidx.annotation.LayoutRes
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.theater_edit_item_area.view.*
import soup.movie.R
import soup.movie.data.model.AreaGroup
import soup.movie.data.model.Theater
import soup.movie.domain.theater.edit.TheaterEditManager.Companion.MAX_ITEMS
import soup.movie.ui.databinding.DataBindingAdapter
import soup.movie.ui.databinding.DataBindingViewHolder
import soup.movie.util.inflate
import soup.movie.util.showToast

class TheaterEditChildListAdapter(
    private val listener: Listener
) : DataBindingAdapter<AreaGroup>() {

    interface Listener {

        fun add(theater: Theater): Boolean

        fun remove(theater: Theater)
    }

    private var selectedIdSet: MutableList<Theater> = arrayListOf()

    override fun onBindViewHolder(holder: DataBindingViewHolder<AreaGroup>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.theaterListView.apply {
            removeAllViews()
            getItem(position)?.theaterList?.map { theater ->
                inflate<Chip>(context, theater.getFilterChipLayout()).apply {
                    text = theater.name
                    val isSelected = selectedIdSet.any { it.id == theater.id }
                    isChecked = isSelected
                    isChipIconVisible = isSelected.not()
                    setOnCheckedChangeListener { _, checked ->
                        if (checked) {
                            if (listener.add(theater)) {
                                selectedIdSet.add(theater)
                                isChipIconVisible = false
                            } else {
                                isChecked = false
                                context.showToast(context.getString(R.string.theater_select_limit_description, MAX_ITEMS))
                            }
                        } else {
                            listener.remove(theater)
                            selectedIdSet.removeAll { it.id == theater.id }
                            isChipIconVisible = true
                        }
                    }
                }
            }?.forEach { addView(it) }
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.theater_edit_item_area

    fun submitList(list: List<AreaGroup>, selectedIdSet: List<Theater>) {
        this.selectedIdSet = selectedIdSet.toMutableList()
        submitList(list)
    }

    @LayoutRes
    private fun Theater.getFilterChipLayout(): Int {
        return when (type) {
            Theater.TYPE_CGV -> R.layout.theater_edit_item_cgv
            Theater.TYPE_LOTTE -> R.layout.theater_edit_item_lotte
            Theater.TYPE_MEGABOX -> R.layout.theater_edit_item_megabox
            else -> throw IllegalArgumentException("$type is not valid type.")
        }
    }
}

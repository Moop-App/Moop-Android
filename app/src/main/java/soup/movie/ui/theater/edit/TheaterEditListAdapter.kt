package soup.movie.ui.theater.edit

import android.widget.Toast
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.item_area_group.view.*
import soup.movie.R
import soup.movie.data.helper.getFilterChipLayout
import soup.movie.data.model.AreaGroup
import soup.movie.ui.helper.databinding.DataBindingAdapter
import soup.movie.ui.helper.databinding.DataBindingViewHolder
import soup.movie.util.inflate

internal class TheaterEditListAdapter :
        DataBindingAdapter<AreaGroup>() {

    private var selectedIdSet: MutableSet<String> = hashSetOf()

    fun getSelectedIdSet(): Set<String> = selectedIdSet

    override fun onBindViewHolder(holder: DataBindingViewHolder<AreaGroup>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.theaterListView.apply {
            removeAllViews()
            getItem(position).theaterList.map { theater ->
                inflate<Chip>(context, theater.getFilterChipLayout()).apply {
                    text = theater.name
                    isChecked = selectedIdSet.contains(theater.code)
                    isChipIconEnabled = !selectedIdSet.contains(theater.code)
                    setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            if (selectedIdSet.size < MAX_ITEMS) {
                                selectedIdSet.add(theater.code)
                                isChipIconEnabled = false
                            } else {
                                this.isChecked = false
                                val message = context.getString(R.string.theater_select_limit_description, MAX_ITEMS)
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            selectedIdSet.remove(theater.code)
                            isChipIconEnabled = true
                        }
                    }
                }
            }.forEach { addView(it) }
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_area_group

    fun submitList(list: List<AreaGroup>, selectedIdSet: Set<String>) {
        this.selectedIdSet = selectedIdSet.toMutableSet()
        submitList(list)
    }

    companion object {

        private const val MAX_ITEMS = 10
    }
}

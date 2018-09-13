package soup.movie.ui.theater.edit

import android.widget.Toast
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.item_area_group.view.*
import soup.movie.R
import soup.movie.data.helper.getFilterChipLayout
import soup.movie.data.model.AreaGroup
import soup.movie.data.model.Theater
import soup.movie.ui.helper.databinding.DataBindingAdapter
import soup.movie.ui.helper.databinding.DataBindingViewHolder
import soup.movie.util.inflate

internal class TheaterEditListAdapter(selectedItems: List<Theater>) :
        DataBindingAdapter<AreaGroup>() {

    private val selectedItemSet: MutableSet<Theater> = selectedItems.toHashSet()

    fun getSelectedTheaters(): List<Theater> = selectedItemSet.toList()

    override fun onBindViewHolder(holder: DataBindingViewHolder<AreaGroup>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.theaterListView.apply {
            removeAllViews()
            getItem(position).theaterList.map {
                inflate<Chip>(context, it.getFilterChipLayout()).apply {
                    text = it.name
                    isChecked = selectedItemSet.contains(it)
                    isChipIconEnabled = !selectedItemSet.contains(it)
                    setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            if (selectedItemSet.size < MAX_ITEMS) {
                                selectedItemSet.add(it)
                                isChipIconEnabled = false
                            } else {
                                this.isChecked = false
                                val message = context.getString(R.string.theater_select_limit_description, MAX_ITEMS)
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            selectedItemSet.remove(it)
                            isChipIconEnabled = true
                        }
                    }
                }
            }.forEach { addView(it) }
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_area_group

    companion object {

        private const val MAX_ITEMS = 10
    }
}

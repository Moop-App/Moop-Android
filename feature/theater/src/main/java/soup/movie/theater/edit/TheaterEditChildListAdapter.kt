package soup.movie.theater.edit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import soup.movie.ext.showToast
import soup.movie.model.Theater
import soup.movie.model.TheaterArea
import soup.movie.theater.R
import soup.movie.theater.databinding.TheaterEditItemAreaBinding
import soup.movie.theater.edit.TheaterEditManager.Companion.MAX_ITEMS
import soup.movie.util.inflate

class TheaterEditChildListAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<TheaterEditChildListAdapter.AreaGroupViewHolder>() {

    interface Listener {

        fun add(theater: Theater): Boolean

        fun remove(theater: Theater)
    }

    private val items = mutableListOf<TheaterArea>()
    private val selectedIdSet: MutableList<Theater> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaGroupViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TheaterEditItemAreaBinding.inflate(layoutInflater, parent, false)
        return AreaGroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AreaGroupViewHolder, position: Int) {
        val item = getItem(position)
        holder.areaView.text = item.area.name
        holder.theaterListView.apply {
            removeAllViews()
            item.theaterList.map { theater ->
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
            }.forEach { addView(it) }
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.theater_edit_item_area

    override fun getItemCount(): Int = items.size

    private fun getItem(position: Int): TheaterArea = items[position]

    fun submitList(list: List<TheaterArea>, selectedIdSet: List<Theater>) {
        this.selectedIdSet.clear()
        this.selectedIdSet.addAll(selectedIdSet)
        this.items.clear()
        this.items.addAll(list)
        notifyDataSetChanged()
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

    class AreaGroupViewHolder(binding: TheaterEditItemAreaBinding) : RecyclerView.ViewHolder(binding.root) {
        val areaView = binding.areaView
        val theaterListView = binding.theaterListView
    }
}

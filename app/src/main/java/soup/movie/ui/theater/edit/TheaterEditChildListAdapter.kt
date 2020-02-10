package soup.movie.ui.theater.edit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.chip.Chip
import soup.movie.R
import soup.movie.model.Theater
import soup.movie.databinding.TheaterEditItemAreaBinding
import soup.movie.domain.theater.edit.TheaterEditManager.Companion.MAX_ITEMS
import soup.movie.model.TheaterArea
import soup.movie.databinding.DataBindingAdapter
import soup.movie.databinding.DataBindingViewHolder
import soup.movie.util.inflate
import soup.movie.ext.showToast

class TheaterEditChildListAdapter(
    private val listener: Listener
) : DataBindingAdapter<TheaterArea>() {

    interface Listener {

        fun add(theater: Theater): Boolean

        fun remove(theater: Theater)
    }

    private var selectedIdSet: MutableList<Theater> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaGroupViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TheaterEditItemAreaBinding.inflate(layoutInflater, parent, false)
        return AreaGroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<TheaterArea>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is AreaGroupViewHolder) {
            holder.theaterListView.apply {
                removeAllViews()
                getItem(position)?.theaterList?.map { theater ->
                    inflate<Chip>(
                        context,
                        theater.getFilterChipLayout()
                    ).apply {
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
    }

    override fun getItemViewType(position: Int): Int = R.layout.theater_edit_item_area

    fun submitList(list: List<TheaterArea>, selectedIdSet: List<Theater>) {
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

    class AreaGroupViewHolder(binding: TheaterEditItemAreaBinding) : DataBindingViewHolder<TheaterArea>(binding) {

        val theaterListView = binding.theaterListView
    }
}

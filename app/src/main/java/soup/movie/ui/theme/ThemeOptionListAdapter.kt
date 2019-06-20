package soup.movie.ui.theme

import android.view.ViewGroup
import soup.movie.R
import soup.movie.ui.databinding.DataBindingAdapter
import soup.movie.ui.databinding.DataBindingViewHolder
import soup.movie.util.setOnDebounceClickListener

class ThemeOptionListAdapter(
    private val listener: (ThemeOptionItemUiModel) -> Unit
) : DataBindingAdapter<ThemeOptionItemUiModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<ThemeOptionItemUiModel> {
        return super.onCreateViewHolder(parent, viewType).apply {
            itemView.setOnDebounceClickListener(delay = 350L) {
                getItem(adapterPosition)?.run(listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.theme_option_item_option
}

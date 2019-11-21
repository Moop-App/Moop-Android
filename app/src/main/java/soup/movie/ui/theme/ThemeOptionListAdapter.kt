package soup.movie.ui.theme

import androidx.databinding.ViewDataBinding
import soup.movie.R
import soup.movie.ui.databinding.DataBindingAdapter
import soup.movie.ui.databinding.DataBindingViewHolder
import soup.movie.util.setOnDebounceClickListener

class ThemeOptionListAdapter(
    private val listener: (ThemeOptionItemUiModel) -> Unit
) : DataBindingAdapter<ThemeOptionItemUiModel>() {

    override fun createViewHolder(binding: ViewDataBinding): DataBindingViewHolder<ThemeOptionItemUiModel> {
        return super.createViewHolder(binding).apply {
            itemView.setOnDebounceClickListener(delay = 350L) {
                getItem(adapterPosition)?.run(listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.theme_option_item_option
}

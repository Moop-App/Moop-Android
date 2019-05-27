package soup.movie.ui.theme

import android.view.ViewGroup
import soup.movie.R
import soup.movie.ui.databinding.DataBindingAdapter
import soup.movie.ui.databinding.DataBindingViewHolder

class ThemeOptionListAdapter(
    private val listener: (ThemeOptionUiModel) -> Unit
) : DataBindingAdapter<ThemeOptionUiModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<ThemeOptionUiModel> {
        return super.onCreateViewHolder(parent, viewType).apply {
            itemView.setOnClickListener {
                listener.invoke(getItem(adapterPosition))
            }
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_theme_option
}

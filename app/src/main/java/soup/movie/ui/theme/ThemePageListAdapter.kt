package soup.movie.ui.theme

import android.view.ViewGroup
import soup.movie.R
import soup.movie.theme.ThemePage
import soup.movie.ui.databinding.DataBindingAdapter
import soup.movie.ui.databinding.DataBindingViewHolder

internal class ThemePageListAdapter(private val listener: (ThemePage) -> Unit) :
        DataBindingAdapter<ThemePage>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<ThemePage> {
        return super.onCreateViewHolder(parent, viewType).apply {
            itemView.setOnClickListener {
                listener.invoke(getItem(adapterPosition))
            }
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_theme_page
}

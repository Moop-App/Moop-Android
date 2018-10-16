package soup.movie.theme.bookmark

import android.view.ViewGroup
import soup.movie.theme.R
import soup.movie.theme.ThemePage
import soup.movie.theme.bookmark.temp.DataBindingAdapter
import soup.movie.theme.bookmark.temp.DataBindingViewHolder

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

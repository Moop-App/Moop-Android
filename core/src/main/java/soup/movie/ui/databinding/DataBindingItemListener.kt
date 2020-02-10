package soup.movie.ui.databinding

import android.view.View

class DataBindingItemListener<T>(
    private val onClick: (view: View, position: Int, item: T) -> Unit,
    private val onLongClick: (view: View, position: Int, item: T) -> Unit = onClick
) {

    fun onItemClick(view: View, position: Int, item: T?) {
        if (item != null) {
            onClick(view, position, item)
        }
    }

    fun onItemLongClick(view: View, position: Int, item: T?) {
        if (item != null) {
            onLongClick(view, position, item)
        }
    }
}
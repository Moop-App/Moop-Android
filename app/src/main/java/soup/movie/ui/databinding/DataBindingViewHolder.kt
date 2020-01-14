package soup.movie.ui.databinding

import android.view.View
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import soup.movie.BR

open class DataBindingViewHolder<T>(
    private val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root) {

    @CallSuper
    open fun bind(item: T?, itemListener: DataBindingItemListener<T>? = null) {
        binding.setVariable(BR.item, item)
        binding.setVariable(BR.position, adapterPosition)
        binding.setVariable(BR.listener, itemListener)
        binding.executePendingBindings()
    }
}

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

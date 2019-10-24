package soup.movie.ui.databinding

import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import soup.movie.BR

open class DataBindingViewHolder<T>(
    //TODO: Change to private access again
    //private val binding: ViewDataBinding
    val binding: ViewDataBinding
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
    private val onClick: (position: Int, item: T) -> Unit,
    private val onLongClick: (position: Int, item: T) -> Unit = onClick
) {

    fun onItemClick(position: Int, item: T?) {
        if (item != null) {
            onClick(position, item)
        }
    }

    fun onItemLongClick(position: Int, item: T?) {
        if (item != null) {
            onLongClick(position, item)
        }
    }
}

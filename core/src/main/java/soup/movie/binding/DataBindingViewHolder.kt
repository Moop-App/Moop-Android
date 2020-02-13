package soup.movie.binding

import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import soup.movie.core.BR
import soup.movie.ui.databinding.DataBindingItemListener

open class DataBindingViewHolder<T>(
    private val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root) {

    @CallSuper
    open fun bind(item: T?, itemListener: DataBindingItemListener<T>? = null) {
        binding.setVariable(BR.item, item)
        binding.executePendingBindings()
    }
}

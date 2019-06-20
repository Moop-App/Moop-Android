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
    open fun bind(item: T?) {
        binding.setVariable(BR.item, item)
        binding.executePendingBindings()
    }
}

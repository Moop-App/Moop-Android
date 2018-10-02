package soup.movie.ui.helper.databinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class AbsDataBindingListAdapter<T, VH: DataBindingViewHolder<T>, VDB: ViewDataBinding>(
        diffCallback: DiffUtil.ItemCallback<T>) :
        ListAdapter<T, VH>(diffCallback) {

    @Suppress("UNCHECKED_CAST")
    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
                layoutInflater, viewType, parent, false)
        return createViewHolder(binding as VDB)
    }

    protected abstract fun createViewHolder(binding: VDB): VH

    @CallSuper
    override fun onBindViewHolder(holder: VH, position: Int) =
            holder.bind(getItem(position))
}

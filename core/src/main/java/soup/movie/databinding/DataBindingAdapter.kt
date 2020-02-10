package soup.movie.databinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class DataBindingAdapter<T> : RecyclerView.Adapter<DataBindingViewHolder<T>>() {

    protected var items = mutableListOf<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return createViewHolder(binding)
    }

    protected open fun createViewHolder(binding: ViewDataBinding): DataBindingViewHolder<T> {
        return DataBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<T>, position: Int) {
        holder.bind(getItem(position))
    }

    fun getItem(position: Int): T? = items.getOrNull(position)

    final override fun getItemCount(): Int = items.size

    fun submitList(list: List<T>) {
        this.items = list.toMutableList()
        notifyDataSetChanged()
    }
}

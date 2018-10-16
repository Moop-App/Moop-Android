package soup.movie.theme.bookmark.temp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class DataBindingAdapter<T> :
        RecyclerView.Adapter<DataBindingViewHolder<T>>() {

    protected var list: MutableList<T> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<T>, position: Int) =
            holder.bind(getItem(position))

    open fun submitList(list: List<T>) {
        this.list = list.toMutableList()
        notifyDataSetChanged()
    }

    fun getItem(position: Int): T = list[position]

    final override fun getItemCount(): Int = list.size
}
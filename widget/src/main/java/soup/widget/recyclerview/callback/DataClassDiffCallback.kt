package soup.widget.recyclerview.callback

import androidx.recyclerview.widget.DiffUtil

class DataClassDiffCallback<T> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}

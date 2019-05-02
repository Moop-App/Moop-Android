package androidx.recyclerview.widget.ext

import androidx.recyclerview.widget.DiffUtil

class IdBasedDiffCallback<T>(
    private val getIdOf: (T) -> String
) : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return getIdOf(oldItem) == getIdOf(newItem)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}

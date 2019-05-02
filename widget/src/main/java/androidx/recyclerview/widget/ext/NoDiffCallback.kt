package androidx.recyclerview.widget.ext

import androidx.recyclerview.widget.DiffUtil

class NoDiffCallback<T> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = true

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = true
}

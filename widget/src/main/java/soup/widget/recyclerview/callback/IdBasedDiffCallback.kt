package soup.widget.recyclerview.callback

import androidx.recyclerview.widget.DiffUtil

class IdBasedDiffCallback<T: HasId> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}

interface HasId {

    val id: String
}

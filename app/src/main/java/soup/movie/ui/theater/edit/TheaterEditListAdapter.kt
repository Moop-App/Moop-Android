package soup.movie.ui.theater.edit

import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_multiple_choice.view.*
import soup.movie.R
import soup.movie.data.model.Theater
import soup.movie.util.inflate

internal class TheaterEditListAdapter(
        private val allItems: List<Theater>, selectedItems: List<Theater>) :
        RecyclerView.Adapter<TheaterEditListAdapter.ViewHolder>() {

    private val selectedItemMap: HashMap<String, Theater>
            = createSelectedItemMapFrom(selectedItems)

    val selectedTheaters: List<Theater>
        get() = ArrayList(selectedItemMap.values)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(parent).also {
                it.itemView.checkedTextView.apply {
                    setOnClickListener { _ ->
                        val theaterItem = allItems[it.adapterPosition]
                        when {
                            isChecked -> {
                                selectedItemMap.remove(theaterItem.code)
                                isChecked = false
                            }
                            selectedItemMap.size < MAX_ITEMS -> {
                                selectedItemMap[theaterItem.code] = theaterItem
                                isChecked = true
                            }
                            else -> {
                                val message = parent.context.getString(R.string.theater_select_limit_description, MAX_ITEMS)
                                Toast.makeText(parent.context, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(allItems[position])
    }

    override fun getItemCount(): Int {
        return allItems.size
    }

    internal inner class ViewHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(parent.inflate(R.layout.item_multiple_choice)) {

        fun bindItem(theater: Theater) {
            itemView.checkedTextView?.apply {
                text = theater.name
                isChecked = selectedItemMap.containsKey(theater.code)
            }
        }
    }

    companion object {

        private const val MAX_ITEMS = 10

        private fun createSelectedItemMapFrom(
                selectedItems: List<Theater>): HashMap<String, Theater> {
            val itemMap = HashMap<String, Theater>()
            for (tc in selectedItems) {
                itemMap[tc.code] = tc
            }
            return itemMap
        }
    }
}

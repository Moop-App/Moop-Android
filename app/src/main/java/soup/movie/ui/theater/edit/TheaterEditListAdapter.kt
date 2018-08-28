package soup.movie.ui.theater.edit

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.item_multiple_choice.view.*
import soup.movie.R
import soup.movie.data.model.Theater

internal class TheaterEditListAdapter(
        private val allItems: List<Theater>,
        selectedItems: List<Theater>)
    : RecyclerView.Adapter<TheaterEditListAdapter.ViewHolder>() {

    private val selectedItemMap: HashMap<String, Theater>
            = createSelectedItemMapFrom(selectedItems)

    val selectedTheaters: List<Theater>
        get() = ArrayList(selectedItemMap.values)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val ctx = parent.context
        val holder = ViewHolder(LayoutInflater.from(ctx)
                .inflate(R.layout.item_multiple_choice, parent, false))
        holder.itemView.text1.let {
            it.setOnClickListener { _ ->
                val theaterItem = allItems[holder.adapterPosition]
                when {
                    it.isChecked -> {
                        selectedItemMap.remove(theaterItem.code)
                        it.isChecked = false
                    }
                    selectedItemMap.size < MAX_ITEMS -> {
                        selectedItemMap[theaterItem.code] = theaterItem
                        it.isChecked = true
                    }
                    else -> {
                        val message = ctx.getString(R.string.theater_select_limit_description, MAX_ITEMS)
                        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            return holder
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(allItems[position])
    }

    override fun getItemCount(): Int {
        return allItems.size
    }

    internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItem(theater: Theater) {
            itemView.text1.let {
                it.text = theater.name
                it.isChecked = selectedItemMap.containsKey(theater.code)
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

package soup.movie.ui.theater.edit

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import soup.movie.R
import soup.movie.data.model.Theater

internal class TheaterEditListAdapter(
        private val allItems: List<Theater>, selectedItems: List<Theater>)
    : RecyclerView.Adapter<TheaterEditListAdapter.ViewHolder>() {

    private val selectedItemMap: HashMap<String, Theater>

    val selectedTheaters: List<Theater>
        get() = ArrayList(selectedItemMap.values)

    init {
        this.selectedItemMap = createSelectedItemMapFrom(selectedItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val ctx = parent.context
        val holder = ViewHolder(LayoutInflater.from(ctx)
                .inflate(android.R.layout.simple_list_item_multiple_choice, parent, false))
        holder.checkedTextView.setOnClickListener { _ ->
            val theaterItem = allItems[holder.adapterPosition]
            holder.checkedTextView.let {
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
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(allItems[position])
    }

    override fun getItemCount(): Int {
        return allItems.size
    }

    internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        @BindView(android.R.id.text1)
        internal lateinit var checkedTextView: CheckedTextView

        init {
            ButterKnife.bind(this, view)
        }

        fun bindItem(theater: Theater) {
            checkedTextView.text = theater.name
            checkedTextView.isChecked = selectedItemMap.containsKey(theater.code)
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

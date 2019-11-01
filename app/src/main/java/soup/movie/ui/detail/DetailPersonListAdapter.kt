package soup.movie.ui.detail

import androidx.recyclerview.widget.ext.AlwaysDiffCallback
import soup.movie.R
import soup.movie.ui.databinding.DataBindingItemListener
import soup.movie.ui.databinding.DataBindingListAdapter
import soup.movie.util.executeWeb

internal class DetailPersonListAdapter :
    DataBindingListAdapter<PersonUiModel>(AlwaysDiffCallback()) {

    override val itemListener = DataBindingItemListener<PersonUiModel>(
        onClick = { v, position, item ->
            v.context.executeWeb("https://m.search.naver.com/search.naver?query=${item.query}")
        }
    )
    override fun getItemViewType(position: Int): Int = R.layout.detail_item_cast_person
}

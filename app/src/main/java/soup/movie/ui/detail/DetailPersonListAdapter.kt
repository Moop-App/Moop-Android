package soup.movie.ui.detail

import androidx.recyclerview.widget.ext.AlwaysDiffCallback
import soup.movie.R
import soup.movie.ui.databinding.DataBindingListAdapter

internal class DetailPersonListAdapter :
    DataBindingListAdapter<PersonUiModel>(AlwaysDiffCallback()) {

    override fun getItemViewType(position: Int): Int = R.layout.detail_item_cast_person
}

package soup.movie.ui.detail

import soup.movie.data.model.Movie
import soup.movie.ui.detail.DetailViewState.ListItem

interface DetailListItemListener {

    fun onInfoClick(item: ListItem)

    fun onTicketClick(item: ListItem)

    fun onMoreTrailersClick(item: Movie)
}

package soup.movie.ui.detail

import soup.movie.data.model.Movie

abstract class DetailListItemListener {

    abstract fun onInfoClick(item: Movie)

    abstract fun onTicketClick(item: Movie)
}

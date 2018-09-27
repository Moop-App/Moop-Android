package soup.movie.ui.detail

import soup.movie.data.model.Movie

interface DetailListItemListener {

    fun onInfoClick(item: Movie)

    fun onTicketClick(item: Movie)

    fun onMoreTrailersClick(item: Movie)
}

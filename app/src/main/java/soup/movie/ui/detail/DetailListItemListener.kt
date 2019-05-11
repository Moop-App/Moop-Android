package soup.movie.ui.detail

import soup.movie.data.model.Movie
import soup.movie.ui.detail.ContentItemUiModel

interface DetailListItemListener {

    fun onInfoClick(item: ContentItemUiModel)

    fun onMoreTrailersClick(item: Movie)
}

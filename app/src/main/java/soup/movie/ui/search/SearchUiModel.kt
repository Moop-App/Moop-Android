package soup.movie.ui.search

import soup.movie.model.Movie

data class SearchContentsUiModel(
    val movies: List<Movie>,
    val hasNoItem: Boolean
)

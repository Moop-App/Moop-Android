package soup.movie.ui.search

import soup.movie.data.model.Movie

class SearchContentsUiModel(
    val isLoading: Boolean,
    val movies: List<Movie>,
    val hasNoItem: Boolean,
    val isError: Boolean
)

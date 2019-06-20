package soup.movie.ui.home

import soup.movie.data.model.Movie

class Request(
    val isNow: Boolean,
    val refresh: Boolean
)

class HomeHeaderUiModel(
    val isNow: Boolean,
    val isPlan: Boolean
)

class HomeContentsUiModel(
    val isLoading: Boolean,
    val movies: List<Movie>,
    val hasNoItem: Boolean,
    val isError: Boolean
)

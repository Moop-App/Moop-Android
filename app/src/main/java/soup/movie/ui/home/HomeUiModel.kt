package soup.movie.ui.home

import soup.movie.data.model.Movie

enum class HomeHeaderUiModel {
    Now, Plan, Favorite
}

class HomeContentsUiModel(
    val movies: List<Movie>
)

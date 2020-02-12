package soup.movie.home

import soup.movie.model.Movie

enum class HomeHeaderUiModel {
    Now, Plan, Favorite
}

class HomeContentsUiModel(
    val movies: List<Movie>
)

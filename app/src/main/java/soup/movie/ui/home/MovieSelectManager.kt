package soup.movie.ui.home

import soup.movie.model.Movie

object MovieSelectManager {

    private var selectedMovie: Movie? = null

    fun getSelectedItem(): Movie? = selectedMovie

    fun select(item: Movie) {
        selectedMovie = item
    }
}

package soup.movie.domain.filter

import kotlinx.coroutines.flow.*
import soup.movie.data.repository.MoopRepository
import soup.movie.model.Movie
import soup.movie.settings.model.GenreFilter

class GetGenreListUseCase(
    private val repository: MoopRepository
) {

    operator fun invoke(): Flow<List<String>> {
        return combine(
            repository.getNowMovieList().mapToGenreSet(),
            repository.getPlanMovieList().mapToGenreSet(),
            ::merge
        ).take(1)
    }

    private fun List<Movie>.toGenreSet(): Set<String> {
        return mapNotNull { it.genres }.flatten().toSet()
    }

    private fun Flow<List<Movie>>.mapToGenreSet(): Flow<Set<String>> {
        return map { it.toGenreSet() }
            .catch { emit(emptySet()) }
    }

    private suspend fun merge(set1: Set<String>, set2: Set<String>): List<String> {
        return mutableListOf<String>().apply {
            addAll(set1 + set2)
            add(GenreFilter.GENRE_ETC)
        }
    }
}

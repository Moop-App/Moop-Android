package soup.movie.domain.filter

import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import soup.movie.data.repository.MoopRepository
import soup.movie.model.Movie
import soup.movie.settings.model.GenreFilter

class GetGenreListUseCase(
    private val repository: MoopRepository
) {

    operator fun invoke(): Observable<List<String>> {
        return Observables
            .zip(
                repository.getNowMovieList().mapToGenreSet(),
                repository.getPlanMovieList().mapToGenreSet(),
                ::merge
            )
            .take(1)
    }

    private fun List<Movie>.toGenreSet(): Set<String> {
        return mapNotNull { it.genres }.flatten().toSet()
    }

    private fun Observable<List<Movie>>.mapToGenreSet(): Observable<Set<String>> {
        return map { it.toGenreSet() }.onErrorReturnItem(emptySet())
    }

    private fun merge(set1: Set<String>, set2: Set<String>): List<String> {
        return mutableListOf<String>().apply {
            addAll(set1 + set2)
            add(GenreFilter.GENRE_ETC)
        }
    }
}

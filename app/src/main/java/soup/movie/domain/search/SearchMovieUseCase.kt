package soup.movie.domain.search

import io.reactivex.Observable
import soup.movie.data.MoopRepository
import soup.movie.domain.Result
import soup.movie.domain.ResultMapper
import soup.movie.domain.search.model.SearchDomainModel

class SearchMovieUseCase(
    private val repository: MoopRepository
): ResultMapper {

    operator fun invoke(query: String): Observable<Result<SearchDomainModel>> {
        return repository.searchMovie(query)
            .map { SearchDomainModel(it) }
            .mapResult()
    }
}

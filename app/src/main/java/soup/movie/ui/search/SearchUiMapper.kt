package soup.movie.ui.search

import soup.movie.domain.Result
import soup.movie.domain.search.model.SearchDomainModel

interface SearchUiMapper {

    fun Result<SearchDomainModel>.toContentsUiModel(): SearchContentsUiModel {
        val data = (this as? Result.Success)?.data?.movies
        return SearchContentsUiModel(
            isLoading = this is Result.Loading,
            movies = data.orEmpty(),
            hasNoItem = data?.isEmpty() == true,
            isError = this is Result.Failure
        )
    }
}

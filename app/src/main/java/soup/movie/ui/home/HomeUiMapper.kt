package soup.movie.ui.home

import soup.movie.domain.Result
import soup.movie.domain.home.model.HomeDomainModel

interface HomeUiMapper {

    fun Request.toHeaderUiModel(): HomeHeaderUiModel {
        return HomeHeaderUiModel(
            isNow = isNow,
            isPlan = isNow.not()
        )
    }

    fun Result<HomeDomainModel>.toContentsUiModel(): HomeContentsUiModel {
        val data = (this as? Result.Success)?.data?.movies
        return HomeContentsUiModel(
            isLoading = this is Result.Loading,
            movies = data.orEmpty(),
            hasNoItem = data?.isEmpty() == true,
            isError = this is Result.Failure
        )
    }
}

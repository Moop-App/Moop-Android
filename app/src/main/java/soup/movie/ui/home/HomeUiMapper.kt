package soup.movie.ui.home

import soup.movie.domain.Result
import soup.movie.domain.home.model.HomeDomainModel

interface HomeUiMapper {

    fun Boolean.toHeaderUiModel(): HomeHeaderUiModel {
        return HomeHeaderUiModel(
            isNow = this,
            isPlan = this.not()
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

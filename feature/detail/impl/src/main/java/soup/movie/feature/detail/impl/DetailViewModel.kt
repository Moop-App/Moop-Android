/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soup.movie.feature.detail.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import soup.movie.common.DefaultDispatcher
import soup.movie.data.repository.MovieRepository
import soup.movie.datetime.MM_DD
import soup.movie.datetime.today
import soup.movie.datetime.yesterday
import soup.movie.feature.detail.DetailScreenKey
import soup.movie.log.Logger
import soup.movie.model.MovieDetailModel
import soup.movie.model.MovieModel
import soup.movie.model.OpenDateAlarmModel
import java.time.temporal.ChronoUnit

@HiltViewModel(assistedFactory = DetailViewModel.Factory::class)
class DetailViewModel @AssistedInject constructor(
    @Assisted private val input: DetailScreenKey.Movie,
    private val repository: MovieRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val movieId: String = input.movieId

    private val _uiModel = MutableStateFlow<DetailUiModel>(DetailUiModel.None)
    val uiModel: StateFlow<DetailUiModel> = _uiModel

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private val _showOpenDateAlarmMessage = MutableStateFlow(false)
    val showOpenDateAlarmMessage: StateFlow<Boolean> = _showOpenDateAlarmMessage

    init {
        viewModelScope.launch {
            _isFavorite.emit(repository.isFavoriteMovie(movieId))
            loadDetail(movieId)
        }
    }

    private suspend fun loadDetail(movieId: String) {
        try {
            val detail = repository.getMovieDetail(movieId)
            renderDetail(detail)
        } catch (t: Throwable) {
            Logger.w(t)
            _uiModel.emit(DetailUiModel.Failure)
        }
    }

    private suspend fun renderDetail(
        detail: MovieDetailModel,
    ) {
        withContext(defaultDispatcher) {
            _uiModel.emit(
                DetailUiModel.Success(
                    header = HeaderUiModel(
                        movie = detail.movie,
                        showTm = detail.showTm ?: 0,
                        nations = detail.nations.orEmpty(),
                        companies = detail.companies.orEmpty(),
                    ),
                    items = detail.toItemsUiModel(),
                ),
            )
        }
    }

    private fun MovieDetailModel.toItemsUiModel(): List<ContentItemUiModel> {
        val items = mutableListOf<ContentItemUiModel>()
        boxOffice?.run {
            items.add(
                BoxOfficeItemUiModel(
                    rank = rank,
                    rankDate = yesterday().MM_DD(),
                    audience = audiAcc,
                    screenDays = movie.screenDays(),
                ),
            )
        }
        imdb?.run {
            items.add(
                ImdbItemUiModel(
                    imdb = star,
                    rottenTomatoes = rt?.star ?: NO_RATING,
                    metascore = mc?.star ?: NO_RATING,
                    webLink = url,
                ),
            )
        }
        items.add(
            TheatersItemUiModel(
                cgv = CgvItemUiModel(
                    movieId = cgv?.id.orEmpty(),
                    hasInfo = cgv != null,
                    rating = cgv?.star ?: NO_RATING,
                    webLink = cgv?.url,
                ),
                lotte = LotteItemUiModel(
                    movieId = lotte?.id.orEmpty(),
                    hasInfo = lotte != null,
                    rating = lotte?.star ?: NO_RATING,
                    webLink = lotte?.url,
                ),
                megabox = MegaboxItemUiModel(
                    movieId = megabox?.id.orEmpty(),
                    hasInfo = megabox != null,
                    rating = megabox?.star ?: NO_RATING,
                    webLink = megabox?.url,
                ),
            ),
        )

        val plot = plot.orEmpty()
        if (plot.isNotBlank()) {
            items.add(PlotItemUiModel(plot = plot))
        }

        val persons = mutableListOf<PersonUiModel>()
        persons.addAll(
            directors.orEmpty().map {
                PersonUiModel(
                    name = it,
                    cast = "감독",
                    query = "감독 $it",
                )
            },
        )
        persons.addAll(
            actors.orEmpty().map {
                val cast = if (it.cast.isEmpty()) "출연" else it.cast
                PersonUiModel(
                    name = it.peopleNm,
                    cast = cast,
                    query = "배우 ${it.peopleNm}",
                )
            },
        )
        if (persons.isNotEmpty()) {
            items.add(CastItemUiModel(persons = persons))
        }

        val trailers = trailers.orEmpty()
        if (trailers.isNotEmpty()) {
            items.add(TrailerHeaderItemUiModel(movieTitle = movie.title))
            items.addAll(
                trailers.map {
                    TrailerItemUiModel(trailer = it)
                },
            )
            items.add(TrailerFooterItemUiModel(movieTitle = movie.title))
        }
        return items
    }

    fun onFavoriteButtonClick(isFavorite: Boolean) {
        val uiModel = _uiModel.value as? DetailUiModel.Success ?: return
        val movie = uiModel.header.movie
        viewModelScope.launch {
            if (isFavorite) {
                repository.addFavoriteMovie(movie)
                if (movie.isPlan) {
                    repository.insertOpenDateAlarms(
                        OpenDateAlarmModel(
                            movie.id,
                            movie.title,
                            movie.openDate,
                        ),
                    )
                    _showOpenDateAlarmMessage.update { true }
                }
            } else {
                repository.removeFavoriteMovie(movie.id)
            }
            _isFavorite.emit(isFavorite)
        }
    }

    fun onOpenDateAlarmMessageShown() {
        _showOpenDateAlarmMessage.update { false }
    }

    fun onRetryClick() {
        viewModelScope.launch {
            loadDetail(movieId)
        }
    }

    companion object {
        private const val NO_RATING = "평점없음"

        private fun MovieModel.screenDays(): Int {
            val openDate = openLocalDate
            if (openDate != null) {
                return ChronoUnit.DAYS.between(openDate, today()).toInt()
            }
            return 0
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(input: DetailScreenKey.Movie): DetailViewModel
    }
}

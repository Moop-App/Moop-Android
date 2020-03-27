package soup.movie.data.repository.internal.mapper

import soup.movie.data.api.response.*
import soup.movie.model.*

internal fun MovieListResponse.toMovieList() = MovieList(
    lastUpdateTime, list.map { it.toMovie() }
)

private fun MovieResponse.toMovie() = Movie(
    id, score, title, posterUrl, openDate, isNow, age, nationFilter, genres, boxOffice,
    theater.toTheaterRatings()
)

private fun TheaterRatingsResponse.toTheaterRatings() = TheaterRatings(cgv, lotte, megabox)

internal fun MovieDetailResponse.toMovieDetail() = MovieDetail(
    id,
    score,
    title,
    posterUrl,
    openDate,
    isNow,
    age,
    nationFilter,
    genres,
    boxOffice?.toBoxOffice(),
    showTm,
    nations,
    directors,
    actors?.map { it.toActor() },
    companies?.map { it.toCompany() },
    cgv?.toCgvInfo(),
    lotte?.toLotteInfo(),
    megabox?.toMegaboxInfo(),
    naver?.toNaverInfo(),
    imdb?.toImdbInfo(),
    rt?.toRottenTomatoInfo(),
    mc?.toMetascoreInfo(),
    plot,
    trailers?.map { it.toTrailer() }
)

private fun BoxOfficeResponse.toBoxOffice() = BoxOffice(rank, audiCnt, audiAcc)
private fun ActorResponse.toActor() = Actor(peopleNm, cast)
private fun CompanyResponse.toCompany() = Company(companyNm, companyPartNm)
private fun CgvInfoResponse.toCgvInfo() = CgvInfo(id, star, url)
private fun LotteInfoResponse.toLotteInfo() = LotteInfo(id, star, url)
private fun MegaboxInfoResponse.toMegaboxInfo() = MegaboxInfo(id, star, url)
private fun NaverInfoResponse.toNaverInfo() = NaverInfo(star, url)
private fun ImdbInfoResponse.toImdbInfo() = ImdbInfo(id, star, url)
private fun RottenTomatoInfoResponse.toRottenTomatoInfo() = RottenTomatoInfo(star)
private fun MetascoreInfoResponse.toMetascoreInfo() = MetascoreInfo(star)
private fun TrailerResponse.toTrailer() = Trailer(youtubeId, title, author, thumbnailUrl)

internal fun TheaterAreaGroupResponse.toTheaterAreaGroup() = TheaterAreaGroup(
    cgv.map { it.toTheaterArea() },
    lotte.map { it.toTheaterArea() },
    megabox.map { it.toTheaterArea() }
)

private fun TheaterAreaResponse.toTheaterArea() = TheaterArea(
    area.toArea(),
    theaterList.map { it.toTheater() }
)
private fun AreaResponse.toArea() = Area(code, name)
private fun TheaterResponse.toTheater() = Theater(type, code, name, lng, lat)

package soup.movie.data.kobis;

import java.util.List;

import io.reactivex.Single;
import soup.movie.data.kobis.model.DailyBoxOfficeRequest;
import soup.movie.data.kobis.model.DailyBoxOfficeResult;
import soup.movie.data.kobis.model.Movie;
import soup.movie.data.kobis.model.MovieListRequest;
import soup.movie.data.kobis.model.WeeklyBoxOfficeRequest;
import soup.movie.data.kobis.model.WeeklyBoxOfficeResult;

public interface IKobisDataSource {

    Single<List<Movie>> getMovieList(MovieListRequest movieListRequest);

    Single<DailyBoxOfficeResult> getDailyBoxOfficeList(DailyBoxOfficeRequest dailyBoxOfficeRequest);

    Single<WeeklyBoxOfficeResult> getWeeklyBoxOfficeList(WeeklyBoxOfficeRequest weeklyBoxOfficeRequest);
}

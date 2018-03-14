package soup.movie.data.source;

import java.util.List;

import io.reactivex.Single;
import soup.movie.data.DailyBoxOfficeRequest;
import soup.movie.data.DailyBoxOfficeResult;
import soup.movie.data.Movie;
import soup.movie.data.MovieListRequest;
import soup.movie.data.WeeklyBoxOfficeRequest;
import soup.movie.data.WeeklyBoxOfficeResult;

public interface MovieDataSource {

    Single<List<Movie>> getMovieList(MovieListRequest movieListRequest);

    Single<DailyBoxOfficeResult> getDailyBoxOfficeList(DailyBoxOfficeRequest dailyBoxOfficeRequest);

    Single<WeeklyBoxOfficeResult> getWeeklyBoxOfficeList(WeeklyBoxOfficeRequest weeklyBoxOfficeRequest);
}

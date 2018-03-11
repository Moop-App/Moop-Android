package soup.movie.data.source;

import java.util.List;

import io.reactivex.Single;
import soup.movie.data.BoxOfficeMovie;
import soup.movie.data.DailyBoxOfficeRequest;
import soup.movie.data.Movie;
import soup.movie.data.MovieListRequest;
import soup.movie.data.WeeklyBoxOfficeRequest;

public interface MovieDataSource {

    Single<List<Movie>> getMovieList(MovieListRequest movieListRequest);

    Single<List<BoxOfficeMovie>> getDailyBoxOfficeList(DailyBoxOfficeRequest dailyBoxOfficeRequest);

    Single<List<BoxOfficeMovie>> getWeeklyBoxOfficeList(WeeklyBoxOfficeRequest weeklyBoxOfficeRequest);
}

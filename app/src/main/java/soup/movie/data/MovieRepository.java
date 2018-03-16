package soup.movie.data;

import java.util.List;

import io.reactivex.Single;
import soup.movie.data.kobis.IKobisDataSource;
import soup.movie.data.kobis.model.DailyBoxOfficeRequest;
import soup.movie.data.kobis.model.DailyBoxOfficeResult;
import soup.movie.data.kobis.model.Movie;
import soup.movie.data.kobis.model.MovieListRequest;
import soup.movie.data.kobis.model.WeeklyBoxOfficeRequest;
import soup.movie.data.kobis.model.WeeklyBoxOfficeResult;

public class MovieRepository implements IKobisDataSource {

    private final IKobisDataSource mKobis;

    public MovieRepository(IKobisDataSource kobisDataSource) {
        mKobis = kobisDataSource;
    }

    @Override
    public Single<List<Movie>> getMovieList(MovieListRequest movieListRequest) {
        return mKobis.getMovieList(movieListRequest);
    }

    @Override
    public Single<DailyBoxOfficeResult> getDailyBoxOfficeList(DailyBoxOfficeRequest dailyBoxOfficeRequest) {
        return mKobis.getDailyBoxOfficeList(dailyBoxOfficeRequest);
    }

    @Override
    public Single<WeeklyBoxOfficeResult> getWeeklyBoxOfficeList(WeeklyBoxOfficeRequest weeklyBoxOfficeRequest) {
        return mKobis.getWeeklyBoxOfficeList(weeklyBoxOfficeRequest);
    }
}

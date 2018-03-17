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
import soup.movie.data.movist.IMovistDataSource;
import soup.movie.data.movist.model.ComingMovieRequest;
import soup.movie.data.movist.model.ComingMovieResponse;
import soup.movie.data.movist.model.MovieDetailRequest;
import soup.movie.data.movist.model.MovieDetailResponse;
import soup.movie.data.movist.model.NowMovieRequest;
import soup.movie.data.movist.model.NowMovieResponse;

public class MovieRepository implements IKobisDataSource, IMovistDataSource {

    private final IKobisDataSource mKobis;
    private final IMovistDataSource mMovist;

    public MovieRepository(IKobisDataSource kobisDataSource, IMovistDataSource movistDataSource) {
        mKobis = kobisDataSource;
        mMovist = movistDataSource;
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

    @Override
    public Single<NowMovieResponse> getNowMovieList(NowMovieRequest nowMovieRequest) {
        return mMovist.getNowMovieList(nowMovieRequest);
    }

    @Override
    public Single<ComingMovieResponse> getComingMovieList(ComingMovieRequest comingMovieRequest) {
        return mMovist.getComingMovieList(comingMovieRequest);
    }

    @Override
    public Single<MovieDetailResponse> getMovieDetail(MovieDetailRequest movieDetailRequest) {
        return mMovist.getMovieDetail(movieDetailRequest);
    }
}

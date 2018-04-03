package soup.movie.data.kobis;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import soup.movie.data.kobis.model.DailyBoxOfficeRequest;
import soup.movie.data.kobis.model.DailyBoxOfficeResponse;
import soup.movie.data.kobis.model.DailyBoxOfficeResult;
import soup.movie.data.kobis.model.Movie;
import soup.movie.data.kobis.model.MovieListResponse;
import soup.movie.data.kobis.model.MovieListResult;
import soup.movie.data.kobis.model.MovieListRequest;
import soup.movie.data.kobis.model.WeeklyBoxOfficeRequest;
import soup.movie.data.kobis.model.WeeklyBoxOfficeResponse;
import soup.movie.data.kobis.model.WeeklyBoxOfficeResult;
import soup.movie.data.kobis.IKobisDataSource;
import soup.movie.data.kobis.service.KobisApiService;
import timber.log.Timber;

public class KobisDataSource implements IKobisDataSource {

    private final KobisApiService mKobisApi;

    public KobisDataSource(@NonNull KobisApiService serverApiService) {
        mKobisApi = serverApiService;
    }

    @Override
    public Single<List<Movie>> getMovieList(MovieListRequest movieListRequest) {
        return mKobisApi.getMovieList(movieListRequest.toQueryMap())
                .doOnSuccess(response -> Timber.d("doOnSuccess: %s", response.toString()))
                .map(MovieListResponse::getMovieListResult)
                .map(MovieListResult::getMovieList)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<DailyBoxOfficeResult> getDailyBoxOfficeList(DailyBoxOfficeRequest dailyBoxOfficeRequest) {
        return mKobisApi.getDailyBoxOfficeList(dailyBoxOfficeRequest.toQueryMap())
                .doOnSuccess(response -> Timber.d("doOnSuccess: %s", response.toString()))
                .map(DailyBoxOfficeResponse::getResult)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<WeeklyBoxOfficeResult> getWeeklyBoxOfficeList(WeeklyBoxOfficeRequest weeklyBoxOfficeRequest) {
        return mKobisApi.getWeeklyBoxOfficeList(weeklyBoxOfficeRequest.toQueryMap())
                .doOnSuccess(response -> Timber.d("doOnSuccess: %s", response.toString()))
                .map(WeeklyBoxOfficeResponse::getResult)
                .subscribeOn(Schedulers.io());
    }
}

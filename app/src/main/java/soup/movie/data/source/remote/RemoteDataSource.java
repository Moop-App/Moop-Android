package soup.movie.data.source.remote;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import soup.movie.data.Movie;
import soup.movie.data.MovieListResponse;
import soup.movie.data.MovieListResult;
import soup.movie.data.MovieListRequest;
import soup.movie.data.source.MovieDataSource;
import soup.movie.data.source.remote.service.KobisApiService;
import timber.log.Timber;

public class RemoteDataSource implements MovieDataSource {

    private final KobisApiService mKobisApi;

    public RemoteDataSource(@NonNull KobisApiService serverApiService) {
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
}

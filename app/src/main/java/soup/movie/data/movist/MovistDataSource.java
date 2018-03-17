package soup.movie.data.movist;

import io.reactivex.Single;
import soup.movie.data.movist.model.ComingMovieRequest;
import soup.movie.data.movist.model.ComingMovieResponse;
import soup.movie.data.movist.model.MovieDetailRequest;
import soup.movie.data.movist.model.MovieDetailResponse;
import soup.movie.data.movist.model.NowMovieRequest;
import soup.movie.data.movist.model.NowMovieResponse;
import soup.movie.data.movist.service.MovistApiService;

public class MovistDataSource implements IMovistDataSource {

    private MovistApiService mMovistApi;

    public MovistDataSource(MovistApiService movistApi) {
        mMovistApi = movistApi;
    }

    @Override
    public Single<NowMovieResponse> getNowMovieList(NowMovieRequest nowMovieRequest) {
        return mMovistApi.getNowMovieList(nowMovieRequest.toQueryMap());
    }

    @Override
    public Single<ComingMovieResponse> getComingMovieList(ComingMovieRequest comingMovieRequest) {
        return mMovistApi.getComingMovieList(comingMovieRequest.toQueryMap());
    }

    @Override
    public Single<MovieDetailResponse> getMovieDetail(MovieDetailRequest movieDetailRequest) {
        return mMovistApi.getMovieDetail(movieDetailRequest.toQueryMap());
    }
}

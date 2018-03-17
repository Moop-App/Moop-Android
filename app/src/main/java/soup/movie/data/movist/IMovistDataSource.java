package soup.movie.data.movist;

import io.reactivex.Single;
import soup.movie.data.movist.model.ComingMovieRequest;
import soup.movie.data.movist.model.ComingMovieResponse;
import soup.movie.data.movist.model.MovieDetailRequest;
import soup.movie.data.movist.model.MovieDetailResponse;
import soup.movie.data.movist.model.NowMovieRequest;
import soup.movie.data.movist.model.NowMovieResponse;

public interface IMovistDataSource {

    Single<NowMovieResponse> getNowMovieList(NowMovieRequest nowMovieRequest);

    Single<ComingMovieResponse> getComingMovieList(ComingMovieRequest comingMovieRequest);

    Single<MovieDetailResponse> getMovieDetail(MovieDetailRequest movieDetailRequest);
}

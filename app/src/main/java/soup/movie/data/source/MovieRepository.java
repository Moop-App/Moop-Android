package soup.movie.data.source;

import java.util.List;

import io.reactivex.Single;
import soup.movie.data.Movie;
import soup.movie.data.MovieListRequest;

public class MovieRepository implements MovieDataSource {

    private final MovieDataSource mRemoteDataSource;

    public MovieRepository(MovieDataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    @Override
    public Single<List<Movie>> getMovieList(MovieListRequest movieListRequest) {
        return mRemoteDataSource.getMovieList(movieListRequest);
    }
}

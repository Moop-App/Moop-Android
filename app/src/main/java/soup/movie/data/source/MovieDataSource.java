package soup.movie.data.source;

import java.util.List;

import io.reactivex.Single;
import soup.movie.data.Movie;
import soup.movie.data.MovieListRequest;

public interface MovieDataSource {

    Single<List<Movie>> getMovieList(MovieListRequest movieListRequest);

}

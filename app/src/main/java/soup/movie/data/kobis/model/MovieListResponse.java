package soup.movie.data.kobis.model;

public class MovieListResponse {

    private MovieListResult movieListResult;

    public MovieListResult getMovieListResult() {
        return movieListResult;
    }

    @Override
    public String toString() {
        return "MovieListResponse{" +
                "movieListResult=" + movieListResult +
                '}';
    }
}

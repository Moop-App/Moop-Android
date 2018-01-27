package soup.movie.data;

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

package soup.movie.data;

import java.util.List;

public class MovieListResult {

    private List<Movie> movieList;
    private int totCnt;
    private String source;

    public List<Movie> getMovieList() {
        return movieList;
    }

    public int getTotCnt() {
        return totCnt;
    }

    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "MovieListResult{" +
                "movieList=" + movieList +
                ", totCnt=" + totCnt +
                ", source='" + source + '\'' +
                '}';
    }
}

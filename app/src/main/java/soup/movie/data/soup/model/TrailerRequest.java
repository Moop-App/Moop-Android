package soup.movie.data.soup.model;

import java.util.HashMap;
import java.util.Map;

import soup.movie.data.core.QueryMapProvider;

public class TrailerRequest implements QueryMapProvider {

    private String movieCode;

    public TrailerRequest(String movieCode) {
        this.movieCode = movieCode;
    }

    public String getMovieCode() {
        return movieCode;
    }

    @Override
    public String toString() {
        return "TrailerRequest{" +
                "movieCode='" + movieCode + '\'' +
                '}';
    }

    @Override
    public Map<String, String> toQueryMap() {
        return new HashMap<>();
    }
}

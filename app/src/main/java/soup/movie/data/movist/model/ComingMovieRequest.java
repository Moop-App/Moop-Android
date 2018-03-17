package soup.movie.data.movist.model;

import java.util.HashMap;
import java.util.Map;

import soup.movie.data.core.QueryMapProvider;

public class ComingMovieRequest implements QueryMapProvider {

    public ComingMovieRequest() {
    }

    @Override
    public String toString() {
        return "ComingMovieRequest{}";
    }

    @Override
    public Map<String, String> toQueryMap() {
        return new HashMap<>();
    }
}

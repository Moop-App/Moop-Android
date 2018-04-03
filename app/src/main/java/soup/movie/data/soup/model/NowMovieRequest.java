package soup.movie.data.soup.model;

import java.util.HashMap;
import java.util.Map;

import soup.movie.data.core.QueryMapProvider;

public class NowMovieRequest implements QueryMapProvider {

    public NowMovieRequest() {
    }

    @Override
    public String toString() {
        return "NowMovieRequest{}";
    }

    @Override
    public Map<String, String> toQueryMap() {
        return new HashMap<>();
    }
}

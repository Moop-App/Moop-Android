package soup.movie.data.soup.model;

import java.util.HashMap;
import java.util.Map;

import soup.movie.data.core.QueryMapProvider;

public class ArtMovieRequest implements QueryMapProvider {

    public ArtMovieRequest() {
    }

    @Override
    public String toString() {
        return "ArtMovieRequest{}";
    }

    @Override
    public Map<String, String> toQueryMap() {
        return new HashMap<>();
    }
}

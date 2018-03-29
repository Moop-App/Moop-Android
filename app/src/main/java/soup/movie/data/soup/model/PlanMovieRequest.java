package soup.movie.data.soup.model;

import java.util.HashMap;
import java.util.Map;

import soup.movie.data.core.QueryMapProvider;

public class PlanMovieRequest implements QueryMapProvider {

    public PlanMovieRequest() {
    }

    @Override
    public String toString() {
        return "PlanMovieRequest{}";
    }

    @Override
    public Map<String, String> toQueryMap() {
        return new HashMap<>();
    }
}
